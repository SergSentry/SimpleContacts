package org.example;

import java.util.*;

public class ContactBook {

    private static final String BLOCK_DIVIDER = "---------------------------------------------------------------------";
    private static final int CONTACT_ID = 1;
    private static final int CONTACT_NAME = 2;
    private static final int CONTACT_PHONE = 3;

    private static Map<Integer, Map<Integer, Object>> contactBook = new HashMap<>();

    public static void main(String[] args) {
        var inputManager = new Scanner(System.in);

        var contactBook = new ContactBook();

        while (true) {
            var id = contactBook.showMenu(inputManager, "1-ADD, 2-PRINT, 0-EXIT", 0, 2);
            if (id == 0)
                break;

            switch (id) {
                case 1: {
                    contactBook.inputContact(inputManager);
                    break;
                }
                case 2: {
                    contactBook.showContact();
                    break;
                }
            }
        }

        inputManager.close();
    }

    public int genId() {
        var id = contactBook.size() + 1;
        while (contactBook.containsKey(id))
            id++;

        return id;
    }

    public void inputContact(Scanner in) {
        System.out.println(BLOCK_DIVIDER);
        System.out.println("Input contact info.");
        System.out.print("Input a name: ");
        String name = in.next().trim();
        System.out.print("Input a phone number: ");
        String phone = in.next().trim();

        addContact(name, phone);
    }

    public int showMenu(Scanner in, String menuCaptions, int min, int max) {
        System.out.println(BLOCK_DIVIDER);
        System.out.print(menuCaptions);
        int id = -1;

        do {
            System.out.format("\nchoose (%d-%d): ", min, max);
            while (!in.hasNextInt()) {
                in.next();
                System.out.format("\nchoose (%d-%d): ", min, max);
            }

            id = in.nextInt();
        } while (id < min && id > max);

        return id;
    }

    public void newContact(String name, String phone) {
        var id = genId();
        var contact = new HashMap<Integer, Object>();
        contact.put(CONTACT_ID, String.valueOf(id));
        contact.put(CONTACT_NAME, name);

        var phones = new ArrayList<String>();
        phones.add(phone);
        contact.put(CONTACT_PHONE, phones);
        contactBook.put(id, contact);
    }

    public void addContact(String name, String phone) {
        var contact = findContactByName(name);

        if (contact == null) {
            newContact(name, phone);
        } else {
            if (contact.containsKey(CONTACT_PHONE)) {
                var value = contact.get(CONTACT_PHONE);
                if (value instanceof ArrayList<?>) {
                    var list = (ArrayList<String>) value;
                    list.add(phone);
                    contact.put(CONTACT_PHONE, list);
                }
            } else
                contact.put(CONTACT_PHONE, phone);
        }
    }

    public Map<Integer, Object> findContactByName(String name) {
        for (var contact : contactBook.values()) {
            if (contact.containsKey(CONTACT_NAME) && name.equalsIgnoreCase((String)contact.get(CONTACT_NAME)))
                return contact;
        }

        return null;
    }

    public void showContact(Map<Integer, Object> contact) {
        System.out.println(BLOCK_DIVIDER);
        System.out.println("Contact info:");

        if (contact.containsKey(CONTACT_ID))
            System.out.format("\tid: %s\n", contact.get(CONTACT_ID));
        else
            System.out.println("\tid: no");

        if (contact.containsKey(CONTACT_NAME))
            System.out.format("\tname: %s\n", contact.get(CONTACT_NAME));
        else
            System.out.println("\tname: no");

        if (contact.containsKey(CONTACT_PHONE)) {
            var phone = contact.get(CONTACT_PHONE);
            if (phone instanceof ArrayList<?>) {
                var list = (ArrayList<String>) phone;
                list.sort(String::compareTo);
                System.out.format("\tphones: %s\n", String.join(", ", list));
            }
        } else
            System.out.println("\tphone: no");
    }

    public static int compare(Map<Integer, Object> contact1, Map<Integer, Object> contact2) {
        var contact1n = contact1.containsKey(CONTACT_PHONE) ?
                ((ArrayList<?>) contact1.get(CONTACT_PHONE)).size() : 0;
        var contact2n = contact2.containsKey(CONTACT_PHONE) ?
                ((ArrayList<?>) contact2.get(CONTACT_PHONE)).size() : 0;

        Comparator comparator = Comparator.reverseOrder();
        return comparator.compare(contact1n, contact2n);
    }

    public void showContact() {
        var contacts = contactBook.values().stream().sorted(ContactBook::compare).toList();
        for (var contact : contacts)
            showContact(contact);
    }
}