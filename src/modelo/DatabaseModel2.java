package modelo;
//my sql
import java.net.ConnectException;


import main.App;
import raven.toast.Notifications;

import com.mysql.cj.xdevapi.Collection;
import com.mysql.cj.xdevapi.DbDoc;
import com.mysql.cj.xdevapi.DbDocImpl;
import com.mysql.cj.xdevapi.DocResult;
import com.mysql.cj.xdevapi.JsonNumber;
import com.mysql.cj.xdevapi.JsonString;
import com.mysql.cj.xdevapi.Schema;
import com.mysql.cj.xdevapi.Session;
import com.mysql.cj.xdevapi.SessionFactory;

public class DatabaseModel2 {
    private String servidor, basededatos, usuario, password;

    public DatabaseModel2(String servidor, String basededatos, String usuario, String password) {
        this.servidor = servidor;
        this.basededatos = basededatos;
        this.usuario = usuario;
        this.password = password;
    }

    public void getConexion() {
        Session session = new SessionFactory().getSession("mysqlx://localhost:33060/Despacho?user=root&password=123456");
        System.err.println("Connected!");
        Schema schema = session.getDefaultSchema();
        System.err.println("Default schema is: " + schema);
        //documentWalkthrough(schema);
    }

    public static void documentWalkthrough(Schema schema) {
        // document walthrough
        Collection coll = schema.createCollection("myBooks", /* reuseExisting? */ true);
        DbDoc newDoc = new DbDocImpl().add("isbn", new JsonString().setValue("12345"));
        newDoc.add("title", new JsonString().setValue("Effi Briest"));
        newDoc.add("author", new JsonString().setValue("Theodor Fontane"));
        newDoc.add("currentlyReadingPage", new JsonNumber().setValue(String.valueOf(42)));
        coll.add(newDoc).execute();

        // note: "$" prefix for document paths is optional. "$.title.somethingElse[0]" is the same as "title.somethingElse[0]" in document expressions
        DocResult docs = coll.find("$.title = 'Effi Briest' and $.currentlyReadingPage > 10").execute();
        DbDoc book = docs.next();
        System.err.println("Currently reading " + ((JsonString) book.get("title")).getString() + " on page "
                + ((JsonNumber) book.get("currentlyReadingPage")).getInteger());

        // increment the page number and fetch it again
        coll.modify("$.isbn = 12345").set("$.currentlyReadingPage", ((JsonNumber) book.get("currentlyReadingPage")).getInteger() + 1).execute();

        docs = coll.find("$.title = 'Effi Briest' and $.currentlyReadingPage > 10").execute();
        book = docs.next();
        System.err.println("Currently reading " + ((JsonString) book.get("title")).getString() + " on page "
                + ((JsonNumber) book.get("currentlyReadingPage")).getInteger());

        // remove the doc
        coll.remove("true").execute();
        System.err.println("Number of books in collection: " + coll.count());

        schema.dropCollection(coll.getName());
    }
}
