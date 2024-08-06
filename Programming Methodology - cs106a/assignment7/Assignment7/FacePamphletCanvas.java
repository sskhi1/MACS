
/*
 * File: FacePamphletCanvas.java
 * -----------------------------
 * This class represents the canvas on which the profiles in the social
 * network are displayed.  NOTE: This class does NOT need to update the
 * display when the window is resized.
 */

import acm.graphics.*;
import java.awt.*;
import java.util.*;

public class FacePamphletCanvas extends GCanvas implements FacePamphletConstants {
	/**
	 * Constructor This method takes care of any initialization needed for the
	 * display
	 */
	public FacePamphletCanvas() {
		// Program starts with introduction: welcome to FacePamphlet.
		GLabel message = new GLabel("Welcome to FacePamphlet");
		message.setFont(MESSAGE_FONT);
		message.setLocation((APPLICATION_WIDTH - 200) / 2 - message.getWidth() / 2,
				APPLICATION_HEIGHT - BOTTOM_MESSAGE_MARGIN - message.getHeight() * 4);
		add(message);
	}

	/**
	 * This method displays a message string near the bottom of the canvas. Every
	 * time this method is called, the previously displayed message (if any) is
	 * replaced by the new message text passed in.
	 */
	public void showMessage(String msg) {
		GLabel message = new GLabel(msg);
		message.setFont(MESSAGE_FONT);
		message.setLocation((APPLICATION_WIDTH - 200) / 2 - message.getWidth() / 2,
				APPLICATION_HEIGHT - BOTTOM_MESSAGE_MARGIN - message.getHeight() * 4);
		add(message);
	}

	/**
	 * This method displays the given profile on the canvas. The canvas is first
	 * cleared of all existing items (including messages displayed near the bottom
	 * of the screen) and then the given profile is displayed. The profile display
	 * includes the name of the user from the profile, the corresponding image (or
	 * an indication that an image does not exist), the status of the user, and a
	 * list of the user's friends in the social network.
	 */
	GLabel friendList;
	GLabel status;

	public void displayProfile(FacePamphletProfile profile) {
		removeAll();
		if (profile == null)
			return;
		if (profile.getName() != null)
			addName(profile.getName());

		if (profile.getImage() == null) {
			addRect();
		} else {
			addPic(profile.getImage());
		}
		addFriends();
		if (profile.getFriends() != null)
			addFriends(profile.getFriends());
		initStatus();
		if (profile.getStatus() != null)
			addStatus(profile);
	}

	// Displays the profile name.
	private GLabel label;

	private void addName(String name) {
		label = new GLabel(name);
		label.setFont(PROFILE_NAME_FONT);
		label.setLocation(LEFT_MARGIN, TOP_MARGIN + label.getHeight());
		label.setColor(Color.BLUE);
		add(label);
	}

	// If you haven't chosen profile picture there will be a rectangle with label:
	// No Image.
	private void addRect() {
		GRect rect = new GRect(IMAGE_WIDTH, IMAGE_HEIGHT);
		add(rect, LEFT_MARGIN, TOP_MARGIN + IMAGE_MARGIN + label.getHeight());
		GLabel noImg = new GLabel("No Image");
		noImg.setFont(PROFILE_IMAGE_FONT);
		add(noImg, LEFT_MARGIN + rect.getWidth() / 2 - noImg.getWidth() / 2,
				TOP_MARGIN + IMAGE_MARGIN + label.getHeight() + rect.getHeight() / 2 + noImg.getHeight() / 2);
	}

	// Add picture if person enters valid jpg.
	private void addPic(GImage img) {
		GImage image = img;
		image.setSize(IMAGE_WIDTH, IMAGE_HEIGHT);
		image.setLocation(LEFT_MARGIN, TOP_MARGIN + IMAGE_MARGIN + label.getHeight());
		add(image);
	}

	// Add "Friends:" label to the canvas.
	private void addFriends() {
		friendList = new GLabel("Friends:");
		friendList.setFont(PROFILE_FRIEND_LABEL_FONT);
		friendList.setLocation(getWidth() / 2, TOP_MARGIN + label.getHeight() + friendList.getHeight());
		add(friendList);
	}

	// Display friends of a profile.
	private void addFriends(Iterator<String> friends) {
		double yCoordinate = TOP_MARGIN + label.getHeight() + friendList.getHeight(); // After every friend added, Y
																						// coordinate of the label
																						// changes.
		while (friends.hasNext()) {
			GLabel friend = new GLabel(friends.next());
			friend.setFont(PROFILE_FRIEND_FONT);
			friend.setLocation(getWidth() / 2, yCoordinate + friend.getHeight());
			add(friend);
			yCoordinate += friend.getHeight();
		}
	}

	GLabel Slabel;

	// Before entering status, there is a label "No current status" on the canvas.
	private void initStatus() {
		Slabel = new GLabel("No current status");
		Slabel.setFont(PROFILE_STATUS_FONT);
		Slabel.setLocation(LEFT_MARGIN,
				TOP_MARGIN + IMAGE_MARGIN + Slabel.getHeight() + STATUS_MARGIN + IMAGE_HEIGHT + Slabel.getHeight());
		add(Slabel);
	}

	// Displays status.
	private void addStatus(FacePamphletProfile profile) {
		String status = profile.getStatus();
		Slabel.setLabel(profile.getName() + " is " + status);
		Slabel.setLocation(LEFT_MARGIN,
				TOP_MARGIN + IMAGE_MARGIN + Slabel.getHeight() + STATUS_MARGIN + Slabel.getHeight() + IMAGE_HEIGHT);
		add(Slabel);
	}

}
