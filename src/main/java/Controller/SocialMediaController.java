// NOTE: this file will handle endpoints
// Will gather the body of the request, then send the body of the request to Service.java

package Controller;

import io.javalin.Javalin;
import io.javalin.http.Context;

// import Service layer class
import Service.Service;
import Model.Account;
import Model.Message;

public class SocialMediaController {
    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();

        Service service = new Service();

        // user registration endpoint
        // url: POST localhost:8080/register
        app.post("register", ctx -> {
            String jsonString = ctx.body();
            Account newUser = service.processRegistration(jsonString);
            if (newUser == null)
            {
                ctx.status(400);
            }
            else
            {
                ctx.status(200);
                ctx.json(newUser);
            }
        });

        // user login
        app.post("login", ctx -> {
            String jsonString = ctx.body();
            Account loginUser = service.processLogin(jsonString);
            if (loginUser == null)
            {
                ctx.status(401);
            }
            else
            {
                ctx.status(200);
                ctx.json(loginUser);
            }
        });


        app.get("example-endpoint", this::exampleHandler);



        return app;
    }

    /**
     * This is an example handler for an example endpoint.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    private void exampleHandler(Context context) {
        context.json("sample text");
    }

    


}