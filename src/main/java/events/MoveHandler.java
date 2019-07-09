package events;

public class MoveHandler extends Handler {
    @Override
    public String handle(String data) {
        String d[] = data.split(SEPARATOR);

        int id  = Integer.parseInt(d[1]);
        int newParentId = Integer.parseInt(d[2]);
        dataDAO.changeParent(newParentId,id);

        return null;
    }
}
