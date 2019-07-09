package events;

import db.DataDAO;

public abstract class Handler {
    public static final String SEPARATOR = "#";
    protected DataDAO dataDAO = new DataDAO();
    public abstract String handle(String data);
}
