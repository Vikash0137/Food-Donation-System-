import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class FoodDonationSystem {
    private static List<Donor> donors = new ArrayList<>();
    private static List<Recipient> recipients = new ArrayList<>();
    private static List<String> feedbackComments = new ArrayList<>();

    public static void main(String[] args) {
        JFrame frame = new JFrame("Food Donation System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1600, 850);
        frame.setLayout(new GridLayout(4, 1));

        JButton donorButton = createStyledButton("Register as Donor", Color.LIGHT_GRAY, "donor-background.jpg");
        JButton recipientButton = createStyledButton("Register as Recipient", Color.LIGHT_GRAY,
                "recipient-background.jpg");
        JButton donateButton = createStyledButton("Donate Food", Color.LIGHT_GRAY, "donate-background.jpg");
        JButton requestButton = createStyledButton("Request Food", Color.LIGHT_GRAY, "request-background.jpg");
        JButton displayButton = createStyledButton("Display Donors and Recipients", Color.LIGHT_GRAY,
                "display-background.jpg");
        JButton editDonorsButton = createStyledButton("Edit Donors List", Color.LIGHT_GRAY,
                "edit-donors-background.jpg");
        JButton editRecipientsButton = createStyledButton("Edit Recipients List", Color.LIGHT_GRAY,
                "edit-recipients-background.jpg");
        JButton helpButton = createStyledButton("Help", Color.LIGHT_GRAY, "help-background.jpg");
        JButton feedbackButton = createStyledButton("Give Feedback", Color.LIGHT_GRAY, "feedback-background.jpg");
        JButton ReviewsButton = createStyledButton("Reviews", Color.LIGHT_GRAY, "reviews-background.jpg");

        donorButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                registerDonor();
            }
        });

        recipientButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                registerRecipient();
            }
        });

        donateButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                donateFood();
            }
        });

        requestButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                requestFood();
            }
        });

        displayButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                displayInformation();
            }
        });

        editDonorsButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                editDonorsList();
            }
        });

        editRecipientsButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                editRecipientsList();
            }
        });
        helpButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                displayHelp();
            }
        });
        feedbackButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                giveFeedback();
            }
        });

        ReviewsButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                displayFeedback();
            }
        });

        frame.add(donorButton);
        frame.add(recipientButton);
        frame.add(donateButton);
        frame.add(requestButton);
        frame.add(displayButton);
        frame.add(editDonorsButton);
        frame.add(editRecipientsButton);
        frame.add(helpButton);
        frame.add(feedbackButton);
        frame.add(ReviewsButton);

        frame.setVisible(true);

    }

    private static JButton createStyledButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setBackground(color);
        button.addActionListener(e -> {
            // Action performed when the button is clicked
        });
        return button;
    }

    private static JButton createStyledButton(String text, Color color, String backgroundImagePath) {
        JButton button = new JButton(text);
        button.setBackground(color);

        // Set background image for the button
        ImageIcon backgroundImage = new ImageIcon("E:\\project\\a.jpeg");
        Image img = backgroundImage.getImage();
        Image scaledImg = img.getScaledInstance(300, 400, Image.SCALE_SMOOTH);
        button.setIcon(new ImageIcon(scaledImg));

        button.addActionListener(e -> {
            // Action performed when the button is clicked
        });
        return button;
    }

    static void registerDonor() {
        String name = JOptionPane.showInputDialog("Enter your name:");
        String address = JOptionPane.showInputDialog("Enter your address:");
        String mobileNumber;

        do {
            mobileNumber = JOptionPane.showInputDialog("Enter your 10-digit mobile number:");
        } while (!isValidMobileNumber(mobileNumber));

        donors.add(new Donor(name, address, mobileNumber));
        JOptionPane.showMessageDialog(null, "Thank you for registering as a donor!");
    }

    static void registerRecipient() {
        String name = JOptionPane.showInputDialog("Enter your name:");
        String address = JOptionPane.showInputDialog("Enter your address:");
        String mobileNumber;

        do {
            mobileNumber = JOptionPane.showInputDialog("Enter your 10-digit mobile number:");
        } while (!isValidMobileNumber(mobileNumber));

        recipients.add(new Recipient(name, address, mobileNumber));
        JOptionPane.showMessageDialog(null, "Thank you for registering as a recipient!");
    }

    static boolean isValidMobileNumber(String mobileNumber) {
        // Check if the mobile number is exactly 10 digits
        return mobileNumber != null && mobileNumber.matches("\\d{10}");
    }

    static void donateFood() {
        if (donors.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No donors registered yet!");
            return;
        }

        if (recipients.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No recipients registered yet!");
            return;
        }

        String[] donorNames = new String[donors.size()];
        for (int i = 0; i < donors.size(); i++) {
            donorNames[i] = donors.get(i).getName();
        }

        String selectedDonor = (String) JOptionPane.showInputDialog(null, "Choose donor:", "Donate Food",
                JOptionPane.QUESTION_MESSAGE, null, donorNames, donorNames[0]);

        if (selectedDonor == null)
            return; // User cancelled
        Donor donor = donors.get(getIndexByName(selectedDonor, donorNames));

        String foodItem = JOptionPane.showInputDialog("Enter food item:");
        foodItem = foodItem.toLowerCase();

        // Check if there is a recipient who has requested the specific item
        Recipient requestingRecipient = findRequestingRecipient(foodItem);
        if (requestingRecipient != null) {
            donor.donate(foodItem);
            requestingRecipient.receiveDonation(foodItem);
            JOptionPane.showMessageDialog(null, "Food donated successfully to " + requestingRecipient.getName() + "!");
        } else {
            donor.addToDonorItemList(foodItem);
            JOptionPane.showMessageDialog(null, "Food item saved in donor's list.");
        }

    }

    static Recipient findRequestingRecipient(String foodItem) {
        for (Recipient recipient : recipients) {
            if (recipient.getRequestedItems().contains(foodItem)) {
                return recipient;
            }
        }
        return null;
    }

    static void requestFood() {
        if (recipients.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No recipients registered yet!");
            return;
        }
        String[] recipientNames = new String[recipients.size()];
        for (int i = 0; i < recipients.size(); i++) {
            recipientNames[i] = recipients.get(i).getName();
        }
        String selectedRecipient = (String) JOptionPane.showInputDialog(null, "Choose recipient:", "Request Food",
                JOptionPane.QUESTION_MESSAGE, null, recipientNames, recipientNames[0]);
        if (selectedRecipient == null)
            return; // User cancelled
        Recipient recipient = recipients.get(getIndexByName(selectedRecipient, recipientNames));

        String foodRequest = JOptionPane.showInputDialog("Enter food request:");
        foodRequest = foodRequest.toLowerCase();
        if (recipient.getReceivedItems().contains(foodRequest)) {
            JOptionPane.showMessageDialog(null, "Food already received by " + recipient.getName() + "!");
        } else {
            Donor matchingDonor = findMatchingDonor(foodRequest);
            if (matchingDonor != null) {
                recipient.receiveDonation(foodRequest);
                matchingDonor.deleteDonorItem(foodRequest);
                JOptionPane.showMessageDialog(null,
                        "Food received successfully from donor " + matchingDonor.getName() + "!");
            } else {
                recipient.request(foodRequest);
                JOptionPane.showMessageDialog(null, "Food request saved!");
            }
        }
    }

    static Donor findMatchingDonor(String foodRequest) {
        for (Donor donor : donors) {
            if (donor.getDonorItemList().contains(foodRequest)) {
                return donor;
            }
        }
        return null;
    }

    static void displayInformation() {
        StringBuilder donorsInfo = new StringBuilder("Donors:\n");
        for (Donor donor : donors) {
            donorsInfo.append("Name: ").append(donor.getName()).append(", Address: ").append(donor.getAddress())
                    .append(", Mobile Number: ").append(donor.getMobileNumber()).append("\n");
            donorsInfo.append("Donated Items: ").append(donor.getDonatedItems()).append("\n");
            donorsInfo.append("Donor Item List: ").append(donor.getDonorItemList()).append("\n");
        }
        StringBuilder recipientsInfo = new StringBuilder("Recipients:\n");
        for (Recipient recipient : recipients) {
            recipientsInfo.append("Name: ").append(recipient.getName()).append(", Address: ")
                    .append(recipient.getAddress())
                    .append(", Mobile Number: ").append(recipient.getMobileNumber()).append("\n");
            recipientsInfo.append("Requested Items: ").append(recipient.getRequestedItems()).append("\n");
            recipientsInfo.append("Received Items: ").append(recipient.getReceivedItems()).append("\n");
        }
        JOptionPane.showMessageDialog(null, donorsInfo.toString() + recipientsInfo.toString());
    }

    static void editDonorsList() {
        if (donors.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No donors registered yet!");
            return;
        }

        String[] donorNames = new String[donors.size()];
        for (int i = 0; i < donors.size(); i++) {
            donorNames[i] = donors.get(i).getName();
        }

        String selectedDonor = (String) JOptionPane.showInputDialog(null, "Choose donor to edit:", "Edit Donors List",
                JOptionPane.QUESTION_MESSAGE, null, donorNames, donorNames[0]);

        if (selectedDonor == null)
            return; // User cancelled
        Donor donor = donors.get(getIndexByName(selectedDonor, donorNames));

        // Edit donor information
        String newName = JOptionPane.showInputDialog("Enter new name (leave blank to keep current name):");
        if (!newName.isBlank()) {
            donor.setName(newName);
        }

        String newAddress = JOptionPane.showInputDialog("Enter new address (leave blank to keep current address):");
        if (!newAddress.isBlank()) {
            donor.setAddress(newAddress);
        }

        String newMobileNumber = JOptionPane
                .showInputDialog("Enter new mobile number (leave blank to keep current mobile number):");
        if (!newMobileNumber.isBlank()) {
            donor.setMobileNumber(newMobileNumber);
        }

        // Edit donor's donated items
        String[] donorDonatedItems = donor.getDonatedItems().toArray(new String[0]);
        String selectedDonatedItem = (String) JOptionPane.showInputDialog(null, "Choose donated item to edit/delete:",
                "Edit Donor's Donated Items",
                JOptionPane.QUESTION_MESSAGE, null, donorDonatedItems, donorDonatedItems[0]);

        if (selectedDonatedItem != null) {
            String action = (String) JOptionPane.showInputDialog(null, "Choose action:", "Edit Donor's Donated Items",
                    JOptionPane.QUESTION_MESSAGE, null, new String[] { "Edit", "Delete" }, "Edit");

            if (action != null) {
                if (action.equals("Edit")) {
                    String newDonatedItem = JOptionPane
                            .showInputDialog("Enter new donated item (leave blank to keep current item):");
                    if (!newDonatedItem.isBlank()) {
                        donor.editDonatedItem(selectedDonatedItem, newDonatedItem);
                        JOptionPane.showMessageDialog(null, "Donated item updated successfully!");
                    }
                } else if (action.equals("Delete")) {
                    donor.deleteDonatedItem(selectedDonatedItem);
                    JOptionPane.showMessageDialog(null, "Donated item deleted successfully!");
                }
            }
        }

        // Edit donor's food item list
        String[] donorFoodItemList = donor.getDonorItemList().toArray(new String[0]);
        String selectedFoodItem = (String) JOptionPane.showInputDialog(null, "Choose food item to edit/delete:",
                "Edit Donor's Food Item List",
                JOptionPane.QUESTION_MESSAGE, null, donorFoodItemList, donorFoodItemList[0]);

        if (selectedFoodItem != null) {
            String action = (String) JOptionPane.showInputDialog(null, "Choose action:", "Edit Donor's Food Item List",
                    JOptionPane.QUESTION_MESSAGE, null, new String[] { "Edit", "Delete" }, "Edit");

            if (action != null) {
                if (action.equals("Edit")) {
                    String newFoodItem = JOptionPane
                            .showInputDialog("Enter new food item (leave blank to keep current item):");
                    if (!newFoodItem.isBlank()) {
                        donor.editDonorItem(selectedFoodItem, newFoodItem);
                        JOptionPane.showMessageDialog(null, "Food item updated successfully!");
                    }
                } else if (action.equals("Delete")) {
                    donor.deleteDonorItem(selectedFoodItem);
                    JOptionPane.showMessageDialog(null, "Food item deleted successfully!");
                }
            }
        }

        JOptionPane.showMessageDialog(null, "Donor information updated successfully!");
    }

    static void editRecipientsList() {
        if (recipients.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No recipients registered yet!");
            return;
        }

        String[] recipientNames = new String[recipients.size()];
        for (int i = 0; i < recipients.size(); i++) {
            recipientNames[i] = recipients.get(i).getName();
        }

        String selectedRecipient = (String) JOptionPane.showInputDialog(null, "Choose recipient to edit:",
                "Edit Recipients List",
                JOptionPane.QUESTION_MESSAGE, null, recipientNames, recipientNames[0]);

        if (selectedRecipient == null)
            return; // User cancelled
        Recipient recipient = recipients.get(getIndexByName(selectedRecipient, recipientNames));

        // Edit recipient information
        String newName = JOptionPane.showInputDialog("Enter new name (leave blank to keep current name):");
        if (!newName.isBlank()) {
            recipient.setName(newName);
        }

        String newAddress = JOptionPane.showInputDialog("Enter new address (leave blank to keep current address):");
        if (!newAddress.isBlank()) {
            recipient.setAddress(newAddress);
        }

        String newMobileNumber = JOptionPane
                .showInputDialog("Enter new mobile number (leave blank to keep current mobile number):");
        if (!newMobileNumber.isBlank()) {
            recipient.setMobileNumber(newMobileNumber);
        }

        // Edit recipient's requested items
        String[] recipientRequestedItems = recipient.getRequestedItems().toArray(new String[0]);
        String selectedRequestedItem = (String) JOptionPane.showInputDialog(null,
                "Choose requested item to edit/delete:", "Edit Recipient's Requested Item List",
                JOptionPane.QUESTION_MESSAGE, null, recipientRequestedItems, recipientRequestedItems[0]);

        if (selectedRequestedItem != null) {
            String action = (String) JOptionPane.showInputDialog(null, "Choose action:",
                    "Edit Recipient's Requested Item List",
                    JOptionPane.QUESTION_MESSAGE, null, new String[] { "Edit", "Delete" }, "Edit");

            if (action != null) {
                if (action.equals("Edit")) {
                    String newRequestedItem = JOptionPane
                            .showInputDialog("Enter new requested item (leave blank to keep current item):");
                    if (!newRequestedItem.isBlank()) {
                        recipient.editRequestedItem(selectedRequestedItem, newRequestedItem);
                        JOptionPane.showMessageDialog(null, "Requested item updated successfully!");
                    }
                } else if (action.equals("Delete")) {
                    recipient.deleteRequestedItem(selectedRequestedItem);
                    JOptionPane.showMessageDialog(null, "Requested item deleted successfully!");
                }
            }
        }

        // Edit recipient's received items
        String[] recipientReceivedItems = recipient.getReceivedItems().toArray(new String[0]);
        String selectedReceivedItem = (String) JOptionPane.showInputDialog(null, "Choose received item to edit/delete:",
                "Edit Recipient's Received Item List",
                JOptionPane.QUESTION_MESSAGE, null, recipientReceivedItems, recipientReceivedItems[0]);

        if (selectedReceivedItem != null) {
            String action = (String) JOptionPane.showInputDialog(null, "Choose action:",
                    "Edit Recipient's Received Item List",
                    JOptionPane.QUESTION_MESSAGE, null, new String[] { "Edit", "Delete" }, "Edit");

            if (action != null) {
                if (action.equals("Edit")) {
                    String newReceivedItem = JOptionPane
                            .showInputDialog("Enter new received item (leave blank to keep current item):");
                    if (!newReceivedItem.isBlank()) {
                        recipient.editReceivedItem(selectedReceivedItem, newReceivedItem);
                        JOptionPane.showMessageDialog(null, "Received item updated successfully!");
                    }
                } else if (action.equals("Delete")) {
                    recipient.deleteReceivedItem(selectedReceivedItem);
                    JOptionPane.showMessageDialog(null, "Received item deleted successfully!");
                }
            }
        }

        JOptionPane.showMessageDialog(null, "Recipient information updated successfully!");
    }

    static void displayHelp() {
        String helpMessage = "Welcome to the Food Donation System!\n\n" +
                "1. To register as a donor, click 'Register as Donor' and fill in your details.\n" +
                "2. To register as a recipient, click 'Register as Recipient' and fill in your details.\n" +
                "3. To donate food, click 'Donate Food' and follow the instructions.\n" +
                "4. To request food, click 'Request Food' and follow the instructions.\n\n" +
                "Thank you for using...!\n\n" +

                "                                                                                          CONTACT US                                                  \n\n"
                +
                " Benktesh Kumar                        Shubham Kumar                             Dhanoj kumar                              Komal Kumari\n"
                +
                " 9471807035                                  8678017311                                     7255871090                                  9546993524\n"
                +
                " benkteshkumar7@gmail.com   shubhamraj1204.ac@gmail.com   dhanojsingh1032@gmail.com   chandrakomal454@gmail.com \n"
                +
                " Banmankhi(Purnia)                    Banmankhi (Purnia)                       Parshurampur(Gopalganj)         Mahua(Hajipur)\n";

        JOptionPane.showMessageDialog(null, helpMessage);
    }

    static int getIndexByName(String name, String[] names) {
        for (int i = 0; i < names.length; i++) {
            if (name.equals(names[i])) {
                return i;
            }
        }
        return -1;
    }

    static void giveFeedback() {
        String feedback = JOptionPane.showInputDialog("Enter your feedback:");
        String name = JOptionPane.showInputDialog("Enter your name:");

        if (!feedback.isBlank() && !name.isBlank()) {
            String comment = name + ": " + feedback;
            feedbackComments.add(comment);
            JOptionPane.showMessageDialog(null, "Thank you for your feedback!");
        } else {
            JOptionPane.showMessageDialog(null, "Invalid feedback or name. Please try again.");
        }
    }

    static void displayFeedback() {
        if (feedbackComments.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No feedback available yet!");
            return;
        }

        StringBuilder feedbackInfo = new StringBuilder("Feedback Comments:\n");
        for (String comment : feedbackComments) {
            feedbackInfo.append(comment).append("\n");
        }

        JOptionPane.showMessageDialog(null, feedbackInfo.toString());
    }
}

class Donor {
    private String name;
    private String address;
    private String mobileNumber;
    private List<String> donatedItems;
    private List<String> donorItemList;

    public Donor(String name, String address, String mobileNumber) {
        this.name = name;
        this.address = address;
        this.mobileNumber = mobileNumber;
        this.donatedItems = new ArrayList<>();
        this.donorItemList = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public List<String> getDonatedItems() {
        return donatedItems;
    }

    public List<String> getDonorItemList() {
        return donorItemList;
    }

    public void donate(String foodItem) {
        donatedItems.add(foodItem);
    }

    public void addToDonorItemList(String foodItem) {
        donorItemList.add(foodItem);
    }

    public void editDonatedItem(String oldItem, String newItem) {
        if (donatedItems.contains(oldItem)) {
            donatedItems.remove(oldItem);
            donatedItems.add(newItem);
        }
    }

    public void deleteDonatedItem(String item) {
        donatedItems.remove(item);
    }

    public void editDonorItem(String oldItem, String newItem) {
        if (donorItemList.contains(oldItem)) {
            donorItemList.remove(oldItem);
            donorItemList.add(newItem);
        }
    }

    public void deleteDonorItem(String item) {
        donorItemList.remove(item);
    }
}

class Recipient {
    private String name;
    private String address;
    private String mobileNumber;
    private List<String> requestedItems;
    private List<String> receivedItems;

    public Recipient(String name, String address, String mobileNumber) {
        this.name = name;
        this.address = address;
        this.mobileNumber = mobileNumber;
        this.requestedItems = new ArrayList<>();
        this.receivedItems = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public List<String> getRequestedItems() {
        return requestedItems;
    }

    public List<String> getReceivedItems() {
        return receivedItems;
    }

    public void request(String foodRequest) {
        requestedItems.add(foodRequest);
    }

    public void receiveDonation(String foodItem) {
        receivedItems.add(foodItem);
    }

    public void editRequestedItem(String oldItem, String newItem) {
        if (requestedItems.contains(oldItem)) {
            requestedItems.remove(oldItem);
            requestedItems.add(newItem);
        }
    }

    public void deleteRequestedItem(String item) {
        requestedItems.remove(item);
    }

    public void editReceivedItem(String oldItem, String newItem) {
        if (receivedItems.contains(oldItem)) {
            receivedItems.remove(oldItem);
            receivedItems.add(newItem);
        }
    }

    public void deleteReceivedItem(String item) {
        receivedItems.remove(item);
    }
}