package in.definex.ChatSystem;

import in.definex.Bot;
import in.definex.Console.Log;
import in.definex.String.XPaths;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Client Class
 * unique identification of each person in each group
 *
 * Created by adam_ on 30-11-2017.
 */
public class Client {

    private String name;
    private String alias;
    private Role role;


    /***
     * Constructor
     *  @param name name of the client in chat (can be phonenumber of saved name)
     * @param alias alias name of client
     * @param role role of the client
     */
    public Client(String name, String alias, Role role) {
        this.name = name;
        this.role = role;
        this.alias = alias;

        chatItemList = new ArrayList<>();
    }

    /**
     * Constructor
     *
     * @param name name of the client in chat (can be phonenumber of saved name)
     * @param role role of the client
     */
    public Client(String name, Role role){
        this.name = name;
        this.role = role;
        this.alias = "";
    }

    /**
     * Getters
     */
    public String getName() {
        return name;
    }
    public Role getRole() {
        return role;
    }

    public String getAlias() {
        return alias;
    }

    public String getFaceName()
    {
        if(alias.isEmpty())
            return name;

        return alias;
    }
    /**
     * Setters
     */
    public void setAlias(String alias) {
        this.alias = alias;
    }


    /***
     * Creates a temporary client for clients who havent registered
     *
     * @param name name of the client in chat (can be phonenumber of saved name)
     * @return Client object
     */
    public static Client createTempAccount(String name){
        return new Client(name, "", Role.Unregistered);
    }

    /**
     * Return super client which have the highest role super admin.
     *
     * @param groupId group id
     * @return client object of super client
     */
    public static Client getSuperClient(String groupId){
        return new Client("ZenoSama", "", Role.SuperAdmin);
    }

    /**
     * Changes the role of the client and saves it into database
     * @param role target role
     */
    public void changeRole(Role role){
        this.role = role;
    }

    /**
     * Returns the webelement of the client, from the which can be clicked on to open the chat
     * @return
     */
    public WebElement getChatWebElement(){
        try {
            return Bot.getWebDriver().findElement(By.xpath(XPaths.getClientNameXPath(name)));
        }catch (NoSuchElementException e){
            Log.e(String.format("Group with id: %s not found, please make sure you have a group with %s%s in the end of the group title,", name
                    ));

            Log.p(e);
        }
        return null;
    }

    /**
     * Maintains the chat cache for the client
     */
    private List<ChatItem> chatItemList;
    public List<ChatItem> getChatItemList(){
        return chatItemList;
    }
    public void addChatItem(ChatItem chatItem){
        chatItemList.add(chatItem);
    }
    public void clearChatCache(){
        chatItemList = new ArrayList<>();
    }

    /**
     * Used in StringActionInitializer to convert string query into client.
     * passed in string in format, a-b, where a is client name, and b is group id
     *
     * @param s client string query
     * @return client object
     */
    public static Client castFromString(String s){
        String[] args = s.split("-");

        return Bot.getClientManager().findClientByName(args[0]);
    }

    /**
     * Role Enum
     */
    public enum Role{
        SuperAdmin(0),
        Admin(1),
        CoAdmin(2),
        Elder(3),
        Member(4),
        Unregistered(5);

        private Integer prestige;

        Role(int prestige){
            this.prestige = prestige;
        }

        public boolean hasPermission(Role minRole){
            return this.prestige <= minRole.prestige;
        }
        public int getPrestige(){
            return prestige;
        }

        public static Role prestigeToRole(int prestige){
            switch (prestige){
                case 0:
                    return SuperAdmin;
                case 1:
                    return Admin;
                case 2:
                    return  CoAdmin;
                case 3:
                    return Elder;
                case 4:
                    return Member;
            }

            return Unregistered;
        }
    }

}
