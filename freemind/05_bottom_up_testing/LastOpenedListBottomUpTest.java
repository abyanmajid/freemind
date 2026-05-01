/*
 * Classes tested: freemind.controller.LastOpenedList   (Step 1 focus)
 *                 freemind.main.Tools                  (lower tier, leaf)
 *                 freemind.controller.LastStateStorageManagement
 *                                                     (same tier sibling)
 * Author:        Abyan Majid
 * Scope:         Bottom-up integration. Tools is tested first (Tier 0),
 *                then Tools + LastOpenedList and Tools +
 *                LastStateStorageManagement (Tier 1), and finally all
 *                three together in a full session round-trip (Tier 2).
 *                No attribute-only tests are included here - that work
 *                is in LastOpenedListTest.java.
 */

import freemind.controller.Controller;
import freemind.controller.LastOpenedList;
import freemind.controller.LastStateStorageManagement;
import freemind.controller.MapModuleManager;
import freemind.controller.actions.generated.instance.MindmapLastStateStorage;
import freemind.main.FreeMindMain;
import freemind.main.Tools;
import freemind.modes.Mode;

import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class LastOpenedListBottomUpTest {

    /*
     * testStateStorage_corruptXmlIsRecovered intentionally feeds garbage
     * XML into LastStateStorageManagement to verify it recovers.
     * FreeMind's Resources.logException logs the caught exception via
     * java.util.logging, which spams the test output. Disable it here.
     */
    @BeforeClass
    public static void silenceJulLogging() {
        LogManager.getLogManager().reset();
        Logger root = Logger.getLogger("");
        root.setLevel(Level.OFF);
        for (Handler h : root.getHandlers()) {
            h.setLevel(Level.OFF);
        }
    }

    // The marshaller emits this exact string for an empty storage; we
    // reuse it to bootstrap fresh LastStateStorageManagement instances.
    private static final String EMPTY_STATE_XML =
            "<?xml version=\"1.0\" encoding=\"UTF-8\"?><mindmap_last_state_map_storage/>";

    /* Same Controller stub as in LastOpenedListTest - the constructor
     * of LastOpenedList needs this to read its max-entries property. */
    private Controller controllerWithMaxLen(String value) {
        FreeMindMain frame = mock(FreeMindMain.class);
        when(frame.getProperty("last_opened_list_length")).thenReturn(value);
        Controller c = mock(Controller.class);
        when(c.getFrame()).thenReturn(frame);
        return c;
    }

    /*
     * Builds a populated state record for one map. The JiBX binding
     * rejects marshalling when "last_selected" is null, so we always
     * set it (to empty string, like FreeMind does when nothing is
     * selected yet).
     */
    private MindmapLastStateStorage state(String restorable, int tabIndex,
                                          float zoom, int x, int y) {
        MindmapLastStateStorage s = new MindmapLastStateStorage();
        s.setRestorableName(restorable);
        s.setLastSelected("");       // required by the JiBX binding
        s.setTabIndex(tabIndex);
        s.setLastZoom(zoom);
        s.setX(x);
        s.setY(y);
        return s;
    }

    // =====================================================================
    // Tier 0 - Tools utilities in isolation (the lowest layer).
    // =====================================================================

    /** safeEquals(null, null) == true, per the contract. */
    @Test
    public void testTools_safeEquals_bothNull() {
        // both sides null - documented as "equal"
        assertTrue(Tools.safeEquals((String) null, (String) null));
    }

    /** safeEquals with exactly one null argument must return false. */
    @Test
    public void testTools_safeEquals_oneNull() {
        // non-null vs null and vice-versa: both must be false
        assertFalse(Tools.safeEquals("abc", null));
        assertFalse(Tools.safeEquals(null, "abc"));
    }

    /** safeEquals delegates to String.equals when both arguments are non-null. */
    @Test
    public void testTools_safeEquals_strings() {
        assertTrue(Tools.safeEquals("MindMap:/a.mm", "MindMap:/a.mm"));
        assertFalse(Tools.safeEquals("MindMap:/a.mm", "MindMap:/b.mm"));
    }

    /** getModeFromRestorable returns the prefix before the first colon. */
    @Test
    public void testTools_getModeFromRestorable() {
        // "MindMap:/path" -> "MindMap"
        assertEquals("MindMap", Tools.getModeFromRestorable("MindMap:/home/u/x.mm"));
        // same for other modes
        assertEquals("Browser", Tools.getModeFromRestorable("Browser:/tmp/y.html"));
    }

    /**
     * getFileNameFromRestorable returns everything after the first colon.
     * Paths that themselves contain colons (Windows drives) must survive.
     */
    @Test
    public void testTools_getFileNameFromRestorable() {
        // a plain unix path
        assertEquals("/home/u/x.mm",
                Tools.getFileNameFromRestorable("MindMap:/home/u/x.mm"));
        // a Windows path - the "C:" part is part of the filename,
        // not a second mode token
        assertEquals("C:/maps/x.mm",
                Tools.getFileNameFromRestorable("MindMap:C:/maps/x.mm"));
    }

    // =====================================================================
    // Tier 1a - Tools + LastOpenedList.
    //
    // These tests verify that LastOpenedList consumes the Tools parsers
    // correctly, i.e. that the "contract" between the two classes holds.
    // =====================================================================

    /**
     * open() must feed exactly the output of Tools.getModeFromRestorable
     * and Tools.getFileNameFromRestorable to the Controller. We drive
     * open() into its "create new mode" branch and check the arguments.
     */
    @Test
    public void testOpenUsesToolsToParseRestorable() throws Exception {
        Controller c = controllerWithMaxLen("10");

        // map is NOT already on a tab, so we fall into the new-mode path
        MapModuleManager mgr = mock(MapModuleManager.class);
        when(c.getMapModuleManager()).thenReturn(mgr);
        when(mgr.tryToChangeToMapModule(any())).thenReturn(false);

        // createNewMode must see exactly the mode token Tools parsed
        when(c.createNewMode("MindMap")).thenReturn(true);

        // Mode.restore must see exactly the filename Tools parsed
        Mode mode = mock(Mode.class);
        when(c.getMode()).thenReturn(mode);

        LastOpenedList list = new LastOpenedList(c, null);

        // the restorable we'll feed in
        String restorable = "MindMap:/home/user/session.mm";
        assertTrue(list.open(restorable));

        // Tools.getModeFromRestorable(restorable) -> "MindMap"
        verify(c).createNewMode("MindMap");
        // Tools.getFileNameFromRestorable(restorable) -> "/home/user/session.mm"
        verify(mode).restore("/home/user/session.mm");
    }

    /**
     * A save() -> load() round-trip on LastOpenedList must preserve the
     * format that Tools can still parse. If save() ever produced something
     * Tools couldn't split back into (mode, filename), open() would break
     * on restored sessions.
     */
    @Test
    public void testSaveLoadPreservesToolsParseability() {
        LastOpenedList original = new LastOpenedList(controllerWithMaxLen("10"), null);

        // populate with two entries
        original.add("MindMap:/a.mm", "a");
        original.add("MindMap:/b.mm", "b");

        // pretend FreeMind shut down and wrote this to disk
        String serialised = original.save();

        // pretend FreeMind restarted and fed it to the new instance
        LastOpenedList restored =
                new LastOpenedList(controllerWithMaxLen("10"), serialised);

        // save() on the restored instance should look identical (or at
        // least contain Tools-parseable entries)
        String roundTripped = restored.save();
        assertTrue("round-tripped save must not be empty",
                roundTripped.length() > 0);

        // for every entry in the round-tripped string, Tools must be
        // able to pull out BOTH a mode and a filename
        for (String entry : roundTripped.split(";")) {
            assertEquals("MindMap", Tools.getModeFromRestorable(entry));
            assertNotNull(Tools.getFileNameFromRestorable(entry));
        }
    }

    // =====================================================================
    // Tier 1b - Tools + LastStateStorageManagement.
    //
    // LastStateStorageManagement uses Tools.marshall / Tools.unMarshall
    // (JiBX) internally, and Tools.safeEquals to match by restorable
    // name. We exercise those paths end-to-end here.
    // =====================================================================

    /** An empty storage round-trips cleanly through Tools.marshall/unMarshall. */
    @Test
    public void testStateStorage_emptyXmlRoundTripsThroughTools() {
        // build from the empty-XML constant, marshal back out
        LastStateStorageManagement mgm = new LastStateStorageManagement(EMPTY_STATE_XML);
        String xml = mgm.getXml();

        // marshal must produce the same canonical empty form we put in
        assertEquals(EMPTY_STATE_XML, xml);

        // and rebuilding from that XML must stay empty
        LastStateStorageManagement reloaded = new LastStateStorageManagement(xml);
        assertTrue(reloaded.getLastOpenList().isEmpty());
    }

    /**
     * Corrupt input must be swallowed by the catch block and recovered
     * to a fresh, empty storage. This is the "it was corrupt" branch.
     */
    @Test
    public void testStateStorage_corruptXmlIsRecovered() {
        // obviously-broken XML: the constructor must NOT propagate
        LastStateStorageManagement mgm =
                new LastStateStorageManagement("<<<not valid xml>>>");

        // recovery path: a brand-new empty storage is installed
        assertTrue(mgm.getLastOpenList().isEmpty());

        // and Tools.marshall on that new storage must still work
        assertNotNull(mgm.getXml());
        assertTrue("recovered storage should still produce valid XML",
                mgm.getXml().startsWith("<?xml"));
    }

    /** getStorage(name) matches entries via Tools.safeEquals (including null input). */
    @Test
    public void testStateStorage_getStorageMatchesByRestorableNameViaTools() {
        LastStateStorageManagement mgm = new LastStateStorageManagement(EMPTY_STATE_XML);

        // populate with two records
        mgm.changeOrAdd(state("MindMap:/a.mm", 0, 1.0f, 10, 20));
        mgm.changeOrAdd(state("MindMap:/b.mm", 1, 1.5f, 30, 40));

        // positive lookup - safeEquals should return true for a matching name
        MindmapLastStateStorage a = mgm.getStorage("MindMap:/a.mm");
        assertNotNull(a);
        assertEquals(10, a.getX());
        assertEquals(20, a.getY());

        // negative lookup - no record by that name, safeEquals returns false
        assertNull(mgm.getStorage("MindMap:/nonexistent.mm"));

        // null-safe lookup - safeEquals(null, "...") is false, so no match
        assertNull(mgm.getStorage(null));
    }

    // =====================================================================
    // Tier 2 - All three classes combined.
    //
    // Simulates a user session: opens three maps, records their per-map
    // state, "shuts down" (serialise), "restarts" (rebuild from the
    // strings), and then checks that the two persistence components
    // still agree on every restorable.
    // =====================================================================

    /**
     * The main combined-functionality test. Drives a realistic close-and-
     * reopen cycle across LastOpenedList + Tools + LastStateStorageManagement.
     */
    @Test
    public void testFullSessionRoundTrip() {
        // --- 1. Build a user session --------------------------------------
        LastOpenedList mru =
                new LastOpenedList(controllerWithMaxLen("10"), null);
        LastStateStorageManagement states =
                new LastStateStorageManagement(EMPTY_STATE_XML);

        // three restorables, one per open map
        String rA = "MindMap:/home/u/alpha.mm";
        String rB = "MindMap:/home/u/beta.mm";
        String rC = "MindMap:/home/u/gamma.mm";

        // user opens alpha, then beta, then gamma (most recent last)
        mru.add(rA, "alpha");
        mru.add(rB, "beta");
        mru.add(rC, "gamma");

        // per-map state: tab index, zoom, scroll X, scroll Y
        states.changeOrAdd(state(rA, 0, 1.0f,   0,   0));
        states.changeOrAdd(state(rB, 1, 1.25f, 50, 100));
        states.changeOrAdd(state(rC, 2, 2.0f, 200, 300));
        states.setLastFocussedTab(2); // gamma is focused at shutdown

        // --- 2. "Shutdown" - persist both components ----------------------
        String mruSerialised    = mru.save();      // string (semicolons)
        String statesSerialised = states.getXml(); // XML (via Tools.marshall)

        // --- 3. "Restart" - rebuild fresh instances from the strings ------
        LastOpenedList mru2 = new LastOpenedList(
                controllerWithMaxLen("10"), mruSerialised);
        LastStateStorageManagement states2 =
                new LastStateStorageManagement(statesSerialised);

        // --- 4. Cross-component invariants --------------------------------

        // (a) MRU order is most-recent-first: gamma, beta, alpha.
        assertEquals(rC + ";" + rB + ";" + rA + ";", mru2.save());

        // (b) Every restorable in the MRU must still have a state record.
        //     This is the key link between the two components, and it
        //     goes through Tools.safeEquals inside getStorage().
        for (String entry : mru2.save().split(";")) {
            assertNotNull("state should survive round-trip for " + entry,
                    states2.getStorage(entry));
        }

        // (c) Per-map state fields must survive byte-for-byte (beta here).
        MindmapLastStateStorage b2 = states2.getStorage(rB);
        assertEquals(1, b2.getTabIndex());
        assertEquals(1.25f, b2.getLastZoom(), 0.0001f);
        assertEquals(50,  b2.getX());
        assertEquals(100, b2.getY());

        // (d) The "focused tab" scalar at the top level survives too.
        assertEquals(2, states2.getLastFocussedTab());

        // (e) getLastOpenList() sorts by tab index, so 0, 1, 2
        //     must come back as alpha, beta, gamma in that order.
        List<MindmapLastStateStorage> openTabs = states2.getLastOpenList();
        assertEquals(3, openTabs.size());
        assertEquals(rA, openTabs.get(0).getRestorableName()); // tab 0
        assertEquals(rB, openTabs.get(1).getRestorableName()); // tab 1
        assertEquals(rC, openTabs.get(2).getRestorableName()); // tab 2
    }

    /**
     * Reopening the same map must NOT duplicate it in either component:
     *   - LastOpenedList collapses the duplicate in its list,
     *   - LastStateStorageManagement updates the existing record in place.
     * Both rely on identifying entries by the restorable name.
     */
    @Test
    public void testReopeningSameMapDedupsInBothComponents() {
        LastOpenedList mru =
                new LastOpenedList(controllerWithMaxLen("10"), null);
        LastStateStorageManagement states =
                new LastStateStorageManagement(EMPTY_STATE_XML);

        String r = "MindMap:/session.mm";

        // first open: fresh entry in both components
        mru.add(r, "session");
        states.changeOrAdd(state(r, 0, 1.0f, 10, 10));

        // user scrolls / zooms, closes and reopens: second open
        mru.add(r, "session");
        states.changeOrAdd(state(r, 0, 3.0f, 777, 888));

        // MRU: exactly one entry (second add collapsed into the first)
        assertEquals("restorable must appear exactly once in MRU",
                r + ";", mru.save());

        // state: updated in place, not duplicated
        MindmapLastStateStorage latest = states.getStorage(r);
        assertEquals(3.0f, latest.getLastZoom(), 0.0001f); // new zoom
        assertEquals(777, latest.getX());                  // new X
        assertEquals(888, latest.getY());                  // new Y

        // only one record total in the state storage
        assertEquals("only one state record should exist",
                1, states.getLastOpenList().size());
    }

    /**
     * The two components have INDEPENDENT eviction policies. The MRU
     * has a tight cap (we set 2); the state storage has its own much
     * larger cap (50). This test makes sure neither component leaks
     * state across the boundary when one evicts but the other doesn't.
     */
    @Test
    public void testIndependentEvictionPolicies() {
        // tight cap on MRU so we can see it evict
        LastOpenedList mru =
                new LastOpenedList(controllerWithMaxLen("2"), null);
        LastStateStorageManagement states =
                new LastStateStorageManagement(EMPTY_STATE_XML);

        // add four maps to both
        for (int i = 0; i < 4; i++) {
            String r = "MindMap:/m" + i + ".mm";
            mru.add(r, "m" + i);
            states.changeOrAdd(state(r, i, 1.0f, i, i));
        }

        // MRU (cap 2) must have evicted m0 and m1, keeping m3, m2
        assertEquals("MindMap:/m3.mm;MindMap:/m2.mm;", mru.save());

        // state storage keeps all four - its cap is much higher
        assertNotNull(states.getStorage("MindMap:/m0.mm"));
        assertNotNull(states.getStorage("MindMap:/m3.mm"));

        // the two maps still in the MRU MUST still have matching state
        assertNotNull(states.getStorage("MindMap:/m2.mm"));
        assertNotNull(states.getStorage("MindMap:/m3.mm"));
    }
}
