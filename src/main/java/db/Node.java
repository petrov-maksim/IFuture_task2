package db;
import events.Handler;

import javax.persistence.*;

@Entity
@Table(name = "nodes")
public class Node {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name")
    private String name;

    @Column(name = "parentid")
    private int parentid;

    @Column(name = "type")
    private boolean type;

    public Node(){}

    public Node(String name, int parentid, boolean type) {
        this.name = name;
        this.parentid = parentid;
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getParentid() {
        return parentid;
    }

    public void setParentid(int parentid) {
        this.parentid = parentid;
    }

    public boolean isType() {
        return type;
    }

    public void setType(boolean type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "id=" + id +
                ", name=" + name +
                ", parentid=" + parentid +
                ", type=" + type + Handler.SEPARATOR;
    }
}
