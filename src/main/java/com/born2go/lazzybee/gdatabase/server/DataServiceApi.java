package com.born2go.lazzybee.gdatabase.server;

import com.born2go.lazzybee.gdatabase.shared.Voca;
import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.api.server.spi.config.Named;

import static com.googlecode.objectify.ObjectifyService.ofy;

/** An endpoint class we are exposing */
@Api(name = "dataServiceApi",
     version = "v1",
     namespace = @ApiNamespace(ownerDomain = "server.gdatabase.lazzybee.born2go.com",
                                ownerName = "server.gdatabase.lazzybee.born2go.com",
                                packagePath=""))
public class DataServiceApi {
	
	/** Get a vocabulary by id */
    @ApiMethod(name = "getVocaById")
    public Voca getVoca(@Named("id") Long id) {
        Voca voca = ofy().load().type(Voca.class).id(id).now();

        return voca;
    }
    
    /** Get a vocabulary by question */
/*    @ApiMethod(name = "getVocaByQ")
    public Voca getVoca(@Named("q") String q) {
        Voca voca = ofy().load().type(Voca.class).filter("q", q).first().now();

        return voca;
    }
*/
}
