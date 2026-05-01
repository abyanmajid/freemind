package plain_click_tests;

import freemind.modes.MindMapNode;
import freemind.modes.mindmapmode.MindMapController;
import freemind.view.mindmapview.MainView;
import freemind.view.mindmapview.NodeView;
import junit.framework.TestCase;

import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

/**
 * Independent Path 4 Test Class for
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
 * Path covered in this test: 1, 2, 3, 5, 7, 8
 * Exactly one node is selected, click is not in the
 * follow-link region, and the node has children.
 *
 * Execution Flow
 * D1 is false: exactly one node selected.
 * D2 is false: click is not in the follow-link region.
 * D3 is false: node has children, so toggleFolded() is called.
 *
 * Expected Result
 * toggleFolded() is called. loadURL() and doubleClick()
 * are never called.
 */
public class PlainClickIndependentPath4Test extends TestCase {

    public void testPath4_hasChildrenToggleFolded() {
        MindMapController controller = mock(MindMapController.class);
        doCallRealMethod().when(controller).plainClick(any(MouseEvent.class));

        List<MindMapNode> selecteds = new ArrayList<>();
        selecteds.add(mock(MindMapNode.class));
        when(controller.getSelecteds()).thenReturn(selecteds);

        MindMapNode mockNode = mock(MindMapNode.class);
        when(mockNode.hasChildren()).thenReturn(true);

        NodeView mockNodeView = mock(NodeView.class);
        when(mockNodeView.getModel()).thenReturn(mockNode);

        MainView mockComponent = mock(MainView.class);
        when(mockComponent.isInFollowLinkRegion(anyDouble())).thenReturn(false);
        when(mockComponent.getNodeView()).thenReturn(mockNodeView);

        MouseEvent mockEvent = mock(MouseEvent.class);
        when(mockEvent.getComponent()).thenReturn(mockComponent);
        when(mockEvent.getX()).thenReturn(10);

        controller.plainClick(mockEvent);

        verify(controller, never()).loadURL();
        verify(controller, never()).doubleClick(any(MouseEvent.class));
        verify(controller).toggleFolded();
    }
}
