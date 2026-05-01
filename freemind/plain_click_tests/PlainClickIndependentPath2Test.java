package plain_click_tests;

import freemind.modes.MindMapNode;
import freemind.modes.mindmapmode.MindMapController;
import freemind.view.mindmapview.MainView;
import junit.framework.TestCase;

import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

/**
 * Independent Path 2 Test Class for
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
 * Path covered in this test: 1, 2, 3, 4, 8
 * Exactly one node is selected and the click is in the
 * follow-link region.
 *
 * Execution Flow
 * D1 is false: exactly one node selected.
 * D2 is true: click is in the follow-link region, so
 * loadURL() is called.
 * D3 is never evaluated.
 *
 * Expected Result
 * loadURL() is called. doubleClick() and toggleFolded()
 * are never called.
 */
public class PlainClickIndependentPath2Test extends TestCase {

    public void testPath2_clickInFollowLinkRegion() {
        MindMapController controller = mock(MindMapController.class);
        doCallRealMethod().when(controller).plainClick(any(MouseEvent.class));

        List<MindMapNode> selecteds = new ArrayList<>();
        selecteds.add(mock(MindMapNode.class));
        when(controller.getSelecteds()).thenReturn(selecteds);

        MainView mockComponent = mock(MainView.class);
        when(mockComponent.isInFollowLinkRegion(anyDouble())).thenReturn(true);

        MouseEvent mockEvent = mock(MouseEvent.class);
        when(mockEvent.getComponent()).thenReturn(mockComponent);
        when(mockEvent.getX()).thenReturn(10);

        controller.plainClick(mockEvent);

        verify(controller).loadURL();
        verify(controller, never()).doubleClick(any(MouseEvent.class));
        verify(controller, never()).toggleFolded();
    }
}
