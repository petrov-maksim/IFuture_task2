package events;

public class UpdateHandler extends Handler {
    @Override
    public String handle(String data) {
        String d[] = data.split(SEPARATOR);

        int parentid = Integer.parseInt(d[1]);
        String newName = d[2];
        dataDAO.update(parentid, newName);

        return null;
    }
}
