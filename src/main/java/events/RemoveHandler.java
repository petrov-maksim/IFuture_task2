package events;

public class RemoveHandler extends Handler {
    @Override
    public String handle(String data) {
        String d[] = data.split(SEPARATOR);

        int id = Integer.parseInt(d[1]);
        if (d[2].equals("true"))
            dataDAO.removeFolder(id);
        else if (d[2].equals("false"))
            dataDAO.removeFile(id);

        return null;
    }
}
