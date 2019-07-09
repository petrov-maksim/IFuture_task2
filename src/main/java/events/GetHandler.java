package events;

import db.Node;

import java.util.List;

public class GetHandler extends Handler {
    @Override
    public String handle(String data) {
        String d[] = data.split(SEPARATOR);
        if (d[0].equals("getRoot"))
            return getChildren(dataDAO.getRoot().getId());
        else if (d[0].equals("getChildren")) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return getChildren(Integer.parseInt(d[1]));
        }

        return null;
    }

    private String getChildren(int id) {
        List<Node> nodes = dataDAO.getChildren(id);
        if (nodes.isEmpty())
            return null;
        nodes.add(0,dataDAO.getNodeById(id));

        nodes.forEach((n)->n.setName(n.getName().replaceAll(" ","<nameSeparator>")));

        return nodes.toString().replaceAll("#, ","#").replaceAll(",","");
    }
}
