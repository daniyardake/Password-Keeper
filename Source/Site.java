/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package passwordkeeper;

/**
 *
 * @author CT-GEN
 */
class Site {

    int id;
    String URL;
    String login;
    String password;
    String note;

    public Site() {
    }

    public Site(int id, String URL, String login, String password, String note) {
        this.id = id;
        this.URL = URL;
        this.login = login;
        this.password = password;
        this.note = note;
    }

    public int getID() {
        return id;
    }

    public String getURL() {
        return URL;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public String getNote() {
        return note;
    }

}
