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
 * Independent Path 3 Test Class for
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
 * Path covered in this test: 1, 2, 3, 5, 6, 8
 * Exactly one node is selected, click is not in the
 * follow-link region, and the node has no children.
 *
 * Execution Flow
 * D1 is false: exactly one node selected.
 * D2 is false: click is not in the follow-link region.
 * D3 is true: node has no children, so doubleClick(e) is called.
 *
 * Expected Result
 * doubleClick(e) is called. loadURL() and toggleFolded()
 * are never called.
 */
public class PlainClickIndependentPath3Test extends TestCase {

    public void testPath3_noChildrenEmulateDoubleClick() {
        MindMapController controller = mock(MindMapController.class);
        doCallRealMethod().when(controller).plainClick(any(MouseEvent.class));

        List<MindMapNode> selecteds = new ArrayList<>();
        selecteds.add(mock(MindMapNode.class));
        when(controller.getSelecteds()).thenReturn(selecteds);

        MindMapNode mockNode = mock(MindMapNode.class);
        when(mockNode.hasChildren()).thenReturn(false);

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
        verify(controller).doubleClick(mockEvent);
        verify(controller, never()).toggleFolded();
    }
}
