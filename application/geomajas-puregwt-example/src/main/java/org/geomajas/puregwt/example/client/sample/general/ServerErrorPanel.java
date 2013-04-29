/*
 * This is part of Geomajas, a GIS framework, http://www.geomajas.org/.
 *
 * Copyright 2008-2013 Geosparc nv, http://www.geosparc.com/, Belgium.
 *
 * The program is available in open source according to the GNU Affero
 * General Public License. All contributions in this program are covered
 * by the Geomajas Contributors License Agreement. For full licensing
 * details, see LICENSE.txt in the project root.
 */

package org.geomajas.puregwt.example.client.sample.general;

import org.geomajas.command.CommandResponse;
import org.geomajas.command.EmptyCommandRequest;
import org.geomajas.gwt.client.command.AbstractCommandCallback;
import org.geomajas.gwt.client.command.GwtCommand;
import org.geomajas.puregwt.client.map.MapPresenter;
import org.geomajas.puregwt.client.service.CommandService;
import org.geomajas.puregwt.client.service.CommandServiceImpl;
import org.geomajas.puregwt.example.client.ContentPanel;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Panel showing server errors.
 *
 * @author Emiel Ackermann
 */
public class ServerErrorPanel extends ContentPanel {
	
	protected CommandService commandService;

	public ServerErrorPanel(MapPresenter mapPresenter) {
		super(mapPresenter);
		commandService = new CommandServiceImpl();
	}

	@Override
	public String getTitle() {
		return "Server error message";
	}

	@Override
	public String getDescription() {
		return "This example shows how a server-side error should look like. " +
				"By pressing the button, a command is executed that then throws an exception." +
				"This exception is than shown on the client.";
	}

	@Override
	public Widget getContentWidget() {
		Button button = new Button("Generate an exception on the server");
		button.addClickHandler(new ClickHandler() {
			
			public void onClick(ClickEvent event) {
				GwtCommand command = new GwtCommand("command.GetException");
				command.setCommandRequest(new EmptyCommandRequest());
				commandService.execute(command, new AbstractCommandCallback<CommandResponse>() {
					
					public void execute(CommandResponse response) {
						// Do nothing... an error message is shown automatically...
					}
				});
			}
		});
		
		HorizontalPanel outer = new HorizontalPanel();
		outer.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		outer.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		
		outer.add(button);
		return outer;
	}

}