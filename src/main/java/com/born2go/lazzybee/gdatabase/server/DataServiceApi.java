package com.born2go.lazzybee.gdatabase.server;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.io.IOException;
import java.util.List;

import com.born2go.lazzybee.gdatabase.shared.Voca;
import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.api.server.spi.config.Named;

/** An endpoint class we are exposing */
@Api(name = "dataServiceApi",
     version = "v1.1",
     title = "LazzyBee Backend Api",
     namespace = @ApiNamespace(ownerDomain = "server.gdatabase.lazzybee.born2go.com",
                                ownerName = "server.gdatabase.lazzybee.born2go.com",
                                packagePath=""))
public class DataServiceApi {
	
	/** Get a vocabulary by id */
    @ApiMethod(name = "getVocaById", path="get_voca_byId")
    public Voca getVocaById(@Named("id") Long id) {
        Voca voca = ofy().load().type(Voca.class).id(id).now();

        return voca;
    }
    
    /** Get a vocabulary by question */
    @ApiMethod(name = "getVocaByQ", path="get_voca_byQ")
    public Voca getVocaByQ(@Named("q") String q) {
        Voca voca = ofy().load().type(Voca.class).filter("q", q).first().now();

        return voca;
    }
    
    /** Get list all of vocabulary */
    @ApiMethod(name = "listVoca")
    public List<Voca> listVoca() {
        List<Voca> list_voca = ofy().load().type(Voca.class).list();

        return list_voca;
    }
    
    /** Save a vocabulary */
    @ApiMethod(name = "saveVoca")
    public void saveVoca(Voca voca) throws IOException{
    	voca.setQ(voca.getQ().toLowerCase());
    	Voca v = ofy().load().type(Voca.class).filter("q", voca.getQ()).first().now();
		if(v == null) 
    		ofy().save().entity(voca).now();
		else {
			if((v.getL_en() == null && v.getL_vn() == null) || (v.getL_en().isEmpty() && v.getL_vn().isEmpty())) {
				if(voca.getL_en() != null && !voca.getL_en().isEmpty())
					v.setL_en(voca.getL_en());
				if(voca.getL_vn() != null && !voca.getL_vn().isEmpty())
					v.setL_vn(voca.getL_vn());
				ofy().save().entity(v).now();
			}
			else
				throw new IOException("INFO: vocabulary already existed.");
		}
    }
    
    boolean verifyVoca(String voca_q) {
		Voca voca = ofy().load().type(Voca.class).filter("q", voca_q).first().now();
		if(voca == null)
			return true;
		else
			return false;
	}

}
