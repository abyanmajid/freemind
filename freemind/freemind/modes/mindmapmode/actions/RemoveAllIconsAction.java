/*FreeMind - A Program for creating and viewing Mindmaps
 *Copyright (C) 2000-2004  Joerg Mueller, Daniel Polansky, Christian Foltin and others.
 *
 *See COPYING for Details
 *
 *This program is free software; you can redistribute it and/or
 *modify it under the terms of the GNU General Public License
 *as published by the Free Software Foundation; either version 2
 *of the License, or (at your option) any later version.
 *
 *This program is distributed in the hope that it will be useful,
 *but WITHOUT ANY WARRANTY; without even the implied warranty of
 *MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *GNU General Public License for more details.
 *
 *You should have received a copy of the GNU General Public License
 *along with this program; if not, write to the Free Software
 *Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 *
 * Created on 29.09.2004
 */


package freemind.modes.mindmapmode.actions;

import java.awt.event.ActionEvent;

import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.KeyStroke;

import freemind.controller.actions.generated.instance.RemoveAllIconsXmlAction;
import freemind.main.Tools;
import freemind.modes.IconInformation;
import freemind.modes.MindMapNode;
import freemind.modes.mindmapmode.MindMapController;

/**
 * @author foltin
 * 
 */
@SuppressWarnings("serial")
public class RemoveAllIconsAction extends NodeGeneralAction implements
		IconInformation {

	/**
     */
	public RemoveAllIconsAction(MindMapController modeController,
			IconAction addIconAction) {
		super(modeController, "remove_all_icons", "images/edittrash.png");
		setDoActionClass(RemoveAllIconsXmlAction.class);
	}

	@Override
	public void xmlActionPerformed(ActionEvent e) {
		int totalBeforeCount = 0;
		boolean hadIcons = false;
		for (Object o : getMindMapController().getSelecteds()) {
			MindMapNode selected = (MindMapNode) o;
			int count = selected.getIcons().size();
			totalBeforeCount += count;
			if (count > 0) {
				hadIcons = true;
			}
		}
		IconAction.logTransition("S0", "S1", "I_node_selected",
				"node_selected", totalBeforeCount, "remove_all_icons", "");
		IconAction.logTransition("S1", "S3", "I_click_remove",
				"remove_evaluated", totalBeforeCount, "remove_all_icons", "");
		if (hadIcons) {
			IconAction.logTransition("S3", "S4", "I_eval_has_icons",
					"icons_removed", totalBeforeCount, "remove_all_icons", "");
		} else {
			IconAction.logTransition("S3", "S5", "I_eval_no_icons",
					"noop_no_icons", totalBeforeCount, "remove_all_icons", "");
		}
		super.xmlActionPerformed(e);
	}

	public String getDescription() {
		return (String) getValue(Action.SHORT_DESCRIPTION);
	}

	public ImageIcon getIcon() {
		return (ImageIcon) getValue(Action.SMALL_ICON);
	}

	public KeyStroke getKeyStroke() {
		return Tools.getKeyStroke(getMindMapController().getFrame()
				.getAdjustableProperty(getKeystrokeResourceName()));
	}

	public String getKeystrokeResourceName() {
		return "keystroke_remove_all_icons";
	}
}
