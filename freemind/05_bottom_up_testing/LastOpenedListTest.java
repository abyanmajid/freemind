/*
 * Class tested: freemind.controller.LastOpenedList
 * Author:       Abyan Majid
 * Scope:        Attribute access via the public API, every public method,
 *               and all state transitions (empty / populated /
 *               duplicate-collapsed / evicted-at-capacity).
 */

import freemind.controller.Controller;
import freemind.controller.LastOpenedList;
import freemind.controller.MapModuleManager;
import freemind.main.FreeMindMain;
import freemind.modes.MindMap;
import freemind.modes.Mode;
import freemind.view.MapModule;

import org.junit.BeforeClass;
import org.junit.Test;

import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class LastOpenedListTest {

    /*
     * Three tests in this file (the constructor-fallback ones) and some in
     * the bottom-up file deliberately feed bad input to verify the catch-
     * block recovery paths. FreeMind's Resources.logException then writes
     * the caught exception to the java.util.logging root logger, which
     * spams the test output. We disable it here for a clean run.
     * Test results are unaffected either way.
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

    /*
     * Small helper: LastOpenedList's constructor reads the property
     * "last_opened_list_length" off Controller.getFrame() to decide the
     * max number of entries to keep. Most tests need a Controller stub
     * that returns a specific value there, so we factor it out.
     */
    private Controller controllerWithMaxLen(String propertyValue) {
        // the FreeMindMain frame is the property bag
        FreeMindMain frame = mock(FreeMindMain.class);
        when(frame.getProperty("last_opened_list_length")).thenReturn(propertyValue);
        // Controller.getFrame() returns that frame
        Controller c = mock(Controller.class);
        when(c.getFrame()).thenReturn(frame);
        return c;
    }

    /*
     * Helper: mapOpened(MapModule) pulls the restorable string off
     * mapModule.getModel() and uses mapModule.toString() as the display
     * name. We only need those two methods stubbed.
     */
    private MapModule mapModuleStub(String restorable, String name) {
        MindMap model = mock(MindMap.class);
        when(model.getRestorable()).thenReturn(restorable);
        MapModule module = mock(MapModule.class);
        when(module.getModel()).thenReturn(model);
        when(module.toString()).thenReturn(name);
        return module;
    }

    // =====================================================================
    // Constructor + the maxEntries attribute (read from a property)
    // =====================================================================

    /** A valid numeric property is parsed and used as the MRU size cap. */
    @Test
    public void testConstructorParsesMaxEntriesFromProperty() {
        // cap = 3 via the stubbed property
        LastOpenedList list = new LastOpenedList(controllerWithMaxLen("3"), null);

        // push four entries - one more than the cap
        list.add("MindMap:/a.mm", "a");
        list.add("MindMap:/b.mm", "b");
        list.add("MindMap:/c.mm", "c");
        list.add("MindMap:/d.mm", "d"); // this should evict "a"

        // expected: most-recent first, "a" gone, three entries left
        assertEquals("cap of 3 should evict the oldest entry",
                "MindMap:/d.mm;MindMap:/c.mm;MindMap:/b.mm;", list.save());
    }

    /**
     * Missing property -> Integer.valueOf(null) throws NumberFormatException,
     * the constructor catches it and falls back to the default cap of 25.
     */
    @Test
    public void testConstructorFallsBackToDefaultMaxWhenPropertyMissing() {
        // property returns null -> NumberFormatException path
        LastOpenedList list = new LastOpenedList(controllerWithMaxLen(null), null);

        // add 26 entries; the default cap is 25 so exactly one must be evicted
        for (int i = 0; i < 26; i++) {
            list.add("MindMap:/f" + i + ".mm", "f" + i);
        }

        // save() ends every entry with ";", so splitting gives us the count
        String saved = list.save();
        int count = saved.split(";").length;
        assertEquals("default cap of 25 should apply when property is missing",
                25, count);
    }

    /** Non-numeric property also routes through the NumberFormatException catch. */
    @Test
    public void testConstructorFallsBackOnNonNumericProperty() {
        // "not-a-number" can't be parsed -> fall back to default (25)
        LastOpenedList list = new LastOpenedList(
                controllerWithMaxLen("not-a-number"), null);

        list.add("MindMap:/x.mm", "x");

        // default cap is generous (25); a single entry sits well under it
        assertEquals("MindMap:/x.mm;", list.save());
    }

    /** Passing null to the constructor's restored-string produces an empty list. */
    @Test
    public void testConstructorWithNullRestoredProducesEmptyList() {
        // restored == null -> load() short-circuits, list stays empty
        LastOpenedList list = new LastOpenedList(controllerWithMaxLen("10"), null);

        // save() on an empty list is the empty string
        assertEquals("", list.save());
    }

    /** A semicolon-separated restored string is parsed back into the list. */
    @Test
    public void testConstructorLoadsRestoredSemicolonList() {
        // simulate what save() would have produced in a previous session
        String restored = "MindMap:/one.mm;MindMap:/two.mm;MindMap:/three.mm";
        LastOpenedList list = new LastOpenedList(controllerWithMaxLen("10"), restored);

        // save() always terminates every entry with ";"
        assertEquals(restored + ";", list.save());
    }

    // =====================================================================
    // add(String, String)
    // =====================================================================

    /** add() prepends: the most-recently-added entry must sit at position 0. */
    @Test
    public void testAddPrependsMostRecent() {
        LastOpenedList list = new LastOpenedList(controllerWithMaxLen("10"), null);

        // "b" is added after "a", so "b" must come first in save()
        list.add("MindMap:/a.mm", "a");
        list.add("MindMap:/b.mm", "b");

        assertEquals("MindMap:/b.mm;MindMap:/a.mm;", list.save());
    }

    /** Re-adding an existing restorable removes the old one and puts it at the head. */
    @Test
    public void testAddDeduplicatesAndMovesToFront() {
        LastOpenedList list = new LastOpenedList(controllerWithMaxLen("10"), null);

        list.add("MindMap:/a.mm", "a");
        list.add("MindMap:/b.mm", "b");

        // re-add "a" - it must move to front, not produce a duplicate
        list.add("MindMap:/a.mm", "a");

        // expected order: a, b (no second copy of a)
        assertEquals("duplicate entries must collapse to a single front entry",
                "MindMap:/a.mm;MindMap:/b.mm;", list.save());
    }

    /** add(null, ...) is a silent no-op (guard at the top of add()). */
    @Test
    public void testAddIgnoresNullRestorable() {
        LastOpenedList list = new LastOpenedList(controllerWithMaxLen("10"), null);

        list.add("MindMap:/a.mm", "a");
        list.add(null, "ignored"); // must not corrupt the list

        // only the non-null entry survives
        assertEquals("MindMap:/a.mm;", list.save());
    }

    /** Size cap: the oldest (tail) entry is evicted when the list grows past cap. */
    @Test
    public void testAddEvictsOldestBeyondCap() {
        // cap = 2
        LastOpenedList list = new LastOpenedList(controllerWithMaxLen("2"), null);

        list.add("MindMap:/a.mm", "a");
        list.add("MindMap:/b.mm", "b");
        list.add("MindMap:/c.mm", "c"); // at size 3 > cap 2 -> evict "a"

        assertEquals("oldest entry should be evicted when cap is exceeded",
                "MindMap:/c.mm;MindMap:/b.mm;", list.save());
    }

    // =====================================================================
    // mapOpened(MapModule)
    // =====================================================================

    /** A well-formed MapModule is converted into an add() of its restorable. */
    @Test
    public void testMapOpenedDelegatesToAdd() {
        LastOpenedList list = new LastOpenedList(controllerWithMaxLen("10"), null);

        // feed a stub with a known restorable
        list.mapOpened(mapModuleStub("MindMap:/opened.mm", "opened"));

        // mapOpened() should have called add() under the hood
        assertEquals("MindMap:/opened.mm;", list.save());
    }

    /** mapOpened(null) is a silent no-op (first null guard). */
    @Test
    public void testMapOpenedNullModuleIsNoop() {
        LastOpenedList list = new LastOpenedList(controllerWithMaxLen("10"), null);

        list.mapOpened(null); // must return without touching the list

        assertEquals("", list.save());
    }

    /** mapOpened on a module with a null model is also a no-op (second null guard). */
    @Test
    public void testMapOpenedNullModelIsNoop() {
        LastOpenedList list = new LastOpenedList(controllerWithMaxLen("10"), null);

        // build a module whose getModel() returns null
        MapModule moduleWithoutModel = mock(MapModule.class);
        when(moduleWithoutModel.getModel()).thenReturn(null);

        list.mapOpened(moduleWithoutModel);

        // second guard kicks in, list untouched
        assertEquals("", list.save());
    }

    // =====================================================================
    // save() / ordering (state observation)
    // =====================================================================

    /** save() on an empty list is the empty string, not null and not ";". */
    @Test
    public void testSaveEmptyList() {
        LastOpenedList list = new LastOpenedList(controllerWithMaxLen("10"), null);

        // no add() calls, no load() input -> empty
        assertEquals("", list.save());
    }

    /** save(load(x)) == x for any semicolon-terminated string x produced by save(). */
    @Test
    public void testSaveLoadRoundTrip() {
        // the canonical shape of a save() output: each entry + ";"
        String restored = "MindMap:/one.mm;MindMap:/two.mm;MindMap:/three.mm;";
        LastOpenedList list = new LastOpenedList(controllerWithMaxLen("10"), restored);

        // round-tripping must preserve the exact byte sequence
        assertEquals(restored, list.save());
    }

    /** Most-recent-first: adding a,b,c in order -> save() returns c,b,a. */
    @Test
    public void testListOrderingMostRecentFirst() {
        LastOpenedList list = new LastOpenedList(controllerWithMaxLen("10"), null);

        list.add("MindMap:/a.mm", "a");
        list.add("MindMap:/b.mm", "b");
        list.add("MindMap:/c.mm", "c");

        // expected: c first (most recent), then b, then a
        assertEquals("MindMap:/c.mm;MindMap:/b.mm;MindMap:/a.mm;", list.save());
    }

    // =====================================================================
    // open(String) - three distinct branches
    // =====================================================================

    /** Branch 1: restoreable is null -> open() returns false. */
    @Test
    public void testOpenReturnsFalseForNullRestorable() throws Exception {
        Controller c = controllerWithMaxLen("10");

        // open() always calls getMapModuleManager() first, even for null,
        // so we have to stub it to avoid an NPE
        MapModuleManager mgr = mock(MapModuleManager.class);
        when(c.getMapModuleManager()).thenReturn(mgr);
        when(mgr.tryToChangeToMapModule(any())).thenReturn(false);

        LastOpenedList list = new LastOpenedList(c, null);

        // null restoreable -> the branch guard "restoreable != null" fails
        assertFalse(list.open(null));
    }

    /** Branch 2: the map is already open in a tab -> open() returns false (tab reused). */
    @Test
    public void testOpenReturnsFalseWhenAlreadyOpen() throws Exception {
        Controller c = controllerWithMaxLen("10");

        // simulate "the requested map is already on a tab" by returning true
        MapModuleManager mgr = mock(MapModuleManager.class);
        when(c.getMapModuleManager()).thenReturn(mgr);
        when(mgr.tryToChangeToMapModule(any())).thenReturn(true); // already open

        LastOpenedList list = new LastOpenedList(c, null);

        assertFalse("already-open means 'we switched tabs; nothing new to load'",
                list.open("MindMap:/already-open.mm"));

        // createNewMode must NOT be called when we just switched tabs
        verify(c, never()).createNewMode(anyString());
    }

    /** Branch 3: not open yet, createNewMode succeeds -> restore() is called, true returned. */
    @Test
    public void testOpenRestoresAndReturnsTrueOnNewMode() throws Exception {
        Controller c = controllerWithMaxLen("10");

        // not already on any tab
        MapModuleManager mgr = mock(MapModuleManager.class);
        when(c.getMapModuleManager()).thenReturn(mgr);
        when(mgr.tryToChangeToMapModule(any())).thenReturn(false);

        // createNewMode("MindMap") succeeds
        when(c.createNewMode(eq("MindMap"))).thenReturn(true);

        // the resulting Mode.restore(filename) is what open() will call
        Mode mode = mock(Mode.class);
        when(c.getMode()).thenReturn(mode);

        LastOpenedList list = new LastOpenedList(c, null);

        boolean result = list.open("MindMap:/home/user/session.mm");

        assertTrue("new-mode branch should return true on success", result);

        // Tools.getFileNameFromRestorable("MindMap:/home/user/session.mm")
        // == "/home/user/session.mm" - open() must pass that to restore()
        verify(mode).restore("/home/user/session.mm");
    }

    /** Branch 3 negative: createNewMode fails -> open() returns false, no restore() call. */
    @Test
    public void testOpenReturnsFalseWhenCreateNewModeFails() throws Exception {
        Controller c = controllerWithMaxLen("10");

        MapModuleManager mgr = mock(MapModuleManager.class);
        when(c.getMapModuleManager()).thenReturn(mgr);
        when(mgr.tryToChangeToMapModule(any())).thenReturn(false);

        // createNewMode returns false - the inner "if" fails
        when(c.createNewMode(anyString())).thenReturn(false);

        LastOpenedList list = new LastOpenedList(c, null);

        assertFalse(list.open("MindMap:/real.mm"));

        // Mode.restore must never be called on this branch
        verify(c, never()).getMode();
    }
}
