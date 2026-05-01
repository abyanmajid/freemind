package plain_click_tests;

import freemind.modes.MindMapNode;
import freemind.modes.mindmapmode.MindMapController;
import junit.framework.TestCase;

import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

/**
 * Independent Path 1 Test Class for
 * MindMapController.plainClick(MouseEvent)
 *
 * Cyclomatic Complexity Analysis
 * This method contains 3 decision predicates:
 * D1: getSelecteds().size() != 1
 * D2: component.isInFollowLinkRegion(e.getX())
 * D3: !node.hasChildren()
 *
 * Cyclomatic Complexity Calculation
 * Number of nodes = 8, Number of edges = 10
 * V(G) = 10 - 8 + 2 = 4
 *
 * Therefore, there are 4 independent paths and each path is
 * tested using a separate test class.
 *
 * Path covered in this test: 1, 2, 8
 * Multiple nodes are selected (getSelecteds().size() != 1).
 *
 * Execution Flow
 * D1 is true: size != 1, method returns immediately.
 * D2 and D3 are never evaluated.
 *
 * Expected Result
 * The method exits early. loadURL(), doubleClick(), and
 * toggleFolded() are never called.
 */
public class PlainClickIndependentPath1Test extends TestCase {

    public void testPath1_multipleNodesSelected() {
        MindMapController controller = mock(MindMapController.class);
        doCallRealMethod().when(controller).plainClick(any(MouseEvent.class));

        List<MindMapNode> selecteds = new ArrayList<>();
        selecteds.add(mock(MindMapNode.class));
        selecteds.add(mock(MindMapNode.class));
        when(controller.getSelecteds()).thenReturn(selecteds);

        MouseEvent mockEvent = mock(MouseEvent.class);

        controller.plainClick(mockEvent);

        verify(controller, never()).loadURL();
        verify(controller, never()).doubleClick(any(MouseEvent.class));
        verify(controller, never()).toggleFolded();
    }
}
