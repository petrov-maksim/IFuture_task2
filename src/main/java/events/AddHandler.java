package events;

import db.Node;

import java.util.List;

public class AddHandler extends Handler {
    @Override
    public String handle(String data) {
        String d[] = data.split(SEPARATOR);

        int id = Integer.parseInt(d[1]);
        String name = d[2];
        boolean type = d[3].equals("true");

        dataDAO.addNode(new Node(name, id, type));

        List<Node> children = dataDAO.getChildren(id);
        int maxId = 0;
        for (Node node : children)
            if (node.getId() > maxId)
                maxId = node.getId();

        return String.valueOf(maxId);
    }
}
