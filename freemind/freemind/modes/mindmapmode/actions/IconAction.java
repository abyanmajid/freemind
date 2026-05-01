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
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.KeyStroke;

import freemind.controller.actions.generated.instance.AddIconAction;
import freemind.main.Resources;
import freemind.main.Tools;
import freemind.modes.IconInformation;
import freemind.modes.MindIcon;
import freemind.modes.MindMapNode;
import freemind.modes.mindmapmode.MindMapController;
import freemind.modes.mindmapmode.actions.xml.actors.AddIconActor;

@SuppressWarnings("serial")
public class IconAction extends MindmapAction implements IconInformation {
	public MindIcon icon;
	private final MindMapController modeController;

	private static final String ICON_FSM_LOG_FILE_NAME = "icon_fsm_events.log";

	public IconAction(MindMapController controller, MindIcon _icon,
			RemoveIconAction removeLastIconAction) {
		super(_icon.getDescription(), _icon.getIcon(), controller);
		this.modeController = controller;
		putValue(Action.SHORT_DESCRIPTION, _icon.getDescription());
		this.icon = _icon;
	}

	public static void logTransition(String fromState, String toState,
			String input, String outcome, int nodeIconCountBefore,
			String button, String iconName) {
		logEvent("transition", "from=" + fromState + "|to=" + toState
				+ "|input=" + input + "|outcome=" + outcome
				+ "|node_icon_count_before=" + nodeIconCountBefore
				+ "|button=" + button + "|icon=" + iconName);
	}

	public static void logEvent(String eventType, String details) {
		BufferedWriter writer = null;
		try {
			File logFile = new File(Resources.getInstance()
					.getFreemindDirectory(), ICON_FSM_LOG_FILE_NAME);
			String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS")
					.format(new Date());
			writer = new BufferedWriter(new FileWriter(logFile, true));
			writer.write(timestamp + "|event=" + eventType + "|" + details);
			writer.newLine();
		} catch (Exception ex) {
			Resources.getInstance().logException(ex,
					"Failed writing icon FSM event log.");
		} finally {
			if (writer != null) {
				try {
					writer.close();
				} catch (Exception closeEx) {
					Resources.getInstance().logException(closeEx,
							"Failed closing icon FSM event log.");
				}
			}
		}
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getID() == ActionEvent.ACTION_FIRST
				&& (e.getModifiers() & ActionEvent.SHIFT_MASK
						& ~ActionEvent.CTRL_MASK & ~ActionEvent.ALT_MASK) != 0) {
			removeAllIcons();
			addLastIcon();
			return;
		}
		if (e == null
				|| (e.getModifiers() & (ActionEvent.CTRL_MASK | ActionEvent.ALT_MASK)) == 0) {
			addLastIcon();
			return;
		}
		// e != null
		if ((e.getModifiers() & ~ActionEvent.SHIFT_MASK
				& ~ActionEvent.CTRL_MASK & ActionEvent.ALT_MASK) != 0) {
			removeIcon(false);
			return;
		}
		if ((e.getModifiers() & ~ActionEvent.SHIFT_MASK & ActionEvent.CTRL_MASK & ~ActionEvent.ALT_MASK) != 0) {
			removeIcon(true);
			return;
		}
	}

	private void addLastIcon() {
		for (MindMapNode selected : modeController.getSelecteds()) {
			int beforeCount = selected.getIcons().size();
			logTransition("S0", "S1", "I_node_selected", "node_selected",
					beforeCount, "specific_icon", icon.getName());
			getAddIconActor().addIcon(selected, icon);
			logTransition("S1", "S2", "I_add_icon", "icon_appended",
					beforeCount, "specific_icon", icon.getName());
		}
	}

	private void removeIcon(boolean removeFirst) {
		for (MindMapNode selected : modeController.getSelecteds()) {
			getAddIconActor().removeIcon(selected, icon, removeFirst);
		}
	}

	private void removeAllIcons() {
		for (MindMapNode selected : modeController.getSelecteds()) {
			if (selected.getIcons().size() > 0) {
				modeController.removeAllIcons(selected);
			}
		}
	}

	


	protected AddIconAction createAddIconAction(MindMapNode node,
			MindIcon icon, int iconIndex) {
		return getAddIconActor().createAddIconAction(node, icon, iconIndex);
	}

	protected AddIconActor getAddIconActor() {
		return getMindMapController().getActorFactory().getAddIconActor();
	}

	public Class<AddIconAction> getDoActionClass() {
		return AddIconAction.class;
	}
	

	public MindIcon getMindIcon() {
		return icon;
	}

	public KeyStroke getKeyStroke() {
		final String keystrokeResourceName = icon.getKeystrokeResourceName();
		final String keyStrokeDescription = getMindMapController().getFrame()
				.getAdjustableProperty(keystrokeResourceName);
		return Tools.getKeyStroke(keyStrokeDescription);
	}

	public String getDescription() {
		return icon.getDescription();
	}

	public ImageIcon getIcon() {
		return icon.getIcon();
	}

	public String getKeystrokeResourceName() {
		return icon.getKeystrokeResourceName();
	}

}
