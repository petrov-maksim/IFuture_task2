package db;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

public class DataDAO {
    public List<Node> getChildren(int parentId){
        List<Node> nodes;
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        String hql = String.format("From Node WHERE parentid = %d", parentId);
        Query query = session.createQuery(hql);
        nodes = query.list();
        session.close();

        return nodes;
    }

    public Node getNodeById(int id){
        Node node;
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        String hql = String.format("From Node WHERE id = %d", id);
        Query query = session.createQuery(hql);
        node = (Node) query.list().get(0);
        session.close();
        return node;
    }

    public Node getRoot(){
        Node root;
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        String hql = "From Node WHERE parentid = 0";
        Query query = session.createQuery(hql);
        root = (Node) query.list().get(0);
        session.close();
        return root;
    }

    public void addNode(Node node){
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction txn = session.beginTransaction();
        session.save(node);
        txn.commit();
        session.close();
    }

    public void removeFolder(int id){
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        String hql = String.format("DELETE Node WHERE id = %d OR parentid = %d",id,id);
        Transaction txn = session.beginTransaction();
        Query query = session.createQuery(hql);
        query.executeUpdate();
        txn.commit();
        session.close();
    }

    public void removeFile(int id){
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        String hql = String.format("DELETE Node WHERE id = %d",id);
        Transaction txn = session.beginTransaction();
        Query query = session.createQuery(hql);
        query.executeUpdate();
        txn.commit();
        session.close();
    }

    public void changeParent(int newParent, int id){
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        String hql = String.format("UPDATE Node SET parentid = %d WHERE id = %d",newParent,id);

        Transaction txn = session.beginTransaction();
        Query query = session.createQuery(hql);
        query.executeUpdate();
        txn.commit();
        session.close();
    }



    public void update(int id, String newName){
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        String hql = String.format("UPDATE Node SET name = '%s' WHERE id = %d",newName,id);

        Transaction txn = session.beginTransaction();
        Query query = session.createQuery(hql);
        query.executeUpdate();
        txn.commit();
        session.close();
    }


    /**
        Fill database with random data
     */
    public static void main(String[] args) {
        DataDAO dataDAO = new DataDAO();

//        Node node1 = new Node("root"    ,0,true);
//        Node node2 = new Node("folder"  ,1,true);
//        Node node3 = new Node("N.pdf"   ,1,false);
//        Node node4 = new Node("folder2" ,1,true);
//        Node node5 = new Node("file.doc",2,false);
//        Node node6 = new Node("T.txt"   ,4,false);
//        Node node7 = new Node("third"   ,4,true);
//        Node node8 = new Node("fourth"  ,7,true);
//        Node node9 = new Node("f.txt"   ,8,false);
//        dataDAO.addNode(node1);
//        dataDAO.addNode(node2);
//        dataDAO.addNode(node3);
//        dataDAO.addNode(node4);
//        dataDAO.addNode(node5);
//        dataDAO.addNode(node6);
//        dataDAO.addNode(node7);
//        dataDAO.addNode(node8);
//        dataDAO.addNode(node9);
    }
}
