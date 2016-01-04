package com.born2go.lazzybee.gdatabase.server;

import static com.googlecode.objectify.ObjectifyService.ofy;

import java.util.List;

import com.born2go.lazzybee.gdatabase.shared.Voca;
import com.born2go.lazzybee.gdatabase.shared.VocaPreview;
import com.born2go.lazzybee.gdatabase.shared.nonentity.UploadTarget;
import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.api.server.spi.config.Named;
import com.google.api.server.spi.response.ConflictException;
import com.google.api.server.spi.response.NotFoundException;
import com.google.appengine.api.blobstore.BlobstoreService;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;

/** An endpoint class we are exposing */
 
@Api(name = "dataServiceApi",
     version = "v1.1",
     title = "LazzyBee Backend Api",
     namespace = @ApiNamespace(ownerDomain = "server.gdatabase.lazzybee.born2go.com",
                                ownerName = "server.gdatabase.lazzybee.born2go.com",
                                packagePath=""))
 
public class DataServiceApi {
	
	private BlobstoreService blobStoreService = BlobstoreServiceFactory
			.getBlobstoreService();

	/** Get a vocabulary by id */
    @ApiMethod(name = "getVocaById", path="get_voca_byId")
    public Voca getVocaById(@Named("id") Long id) throws NotFoundException {
        Voca voca = ofy().load().type(Voca.class).id(id).now();
        if(voca == null) {
        	String message = "No entity exists with ID: " + id;
        	throw new NotFoundException(message);
        }
        else {
        	voca.setCheck(true);
        	return voca;
        }
    }
    
    /** Get a vocabulary by question 
     * @throws NotFoundException */
    @ApiMethod(name = "getVocaByQ", path="get_voca_byQ")
    public Voca getVocaByQ(@Named("q") String q) throws NotFoundException {
        Voca voca = ofy().load().type(Voca.class).filter("q", q).first().now();
        if(voca == null) {
        	String message = "No entity exists with question: " + q;
        	throw new NotFoundException(message);
        }
        else {
        	voca.setCheck(true);
        	return voca;
        }
    }
 
    
    /** Get list all of vocabulary */
    @ApiMethod(name = "listVoca")
    public List<Voca> listVoca() {
        List<Voca> list_voca = ofy().load().type(Voca.class).filter("level <", 8).list();
        return list_voca;
    }

    
    /** Save a vocabulary */
    @ApiMethod(name = "saveVoca")
    public void saveVoca(Voca voca) throws ConflictException{
    	voca.setQ(voca.getQ().toLowerCase());
    	Voca v = ofy().load().type(Voca.class).filter("q", voca.getQ()).first().now(); 
    	VocaPreview vp = ofy().load().type(VocaPreview.class).filter("q", voca.getQ()).first().now();
    	if(v == null && vp == null)
    		ofy().save().entity(voca).now();
    	else {
    		String message = voca.getQ() + " already existed";
    		throw new ConflictException(message);
    	}
    }
    
    /** update answer a vocabulary */
    @ApiMethod(name = "updateA", path="update_A")
    public void updateA(Voca voca) throws NotFoundException{
    	voca.setQ(voca.getQ().toLowerCase());
    	Voca v = ofy().load().type(Voca.class).filter("q", voca.getQ()).first().now();
		if(v != null) {
			v.setA(voca.getA());
			ofy().save().entity(v);
		}
		else {
			String message = voca.getQ() + " not found";
        	throw new NotFoundException(message);
		}
    }
    
    /** update dictionary a vocabulary */
    @ApiMethod(name = "updateD", path="update_D")
    public void updateD(Voca voca) throws NotFoundException{
    	voca.setQ(voca.getQ().toLowerCase());
    	Voca v = ofy().load().type(Voca.class).filter("q", voca.getQ()).first().now();
		if(v != null) {
			v.setL_en(voca.getL_en());
			v.setL_vn(voca.getL_vn());
			ofy().save().entity(v);
		}
		else {
			String message = voca.getQ() + " not found";
        	throw new NotFoundException(message);
		}
    }
    
    /**
     * Find Voca by id
     * @param id
     * @param orderSearch <b>true</b> Voca -> VocaPreview, <b>false</b> VocaPreview -> Voca
     * @return
     * @throws NotFoundException
     */
    @ApiMethod(name = "findVocaById", path="find_voca_byId")
    public Voca findVocaById(@Named("id") Long id, @Named("orderSearch") Boolean orderSearch) throws NotFoundException {
       if(orderSearch) {
    	   Voca v = ofy().load().type(Voca.class).id(id).now();
    	   if(v != null) {
    		   v.setCheck(true);
    		   return v;
    	   }
    	   else {
    		   VocaPreview vp = ofy().load().type(VocaPreview.class).id(id).now();
    		   if(vp != null) {
    			   Voca voca = new Voca();
    			   voca.getVocaPreviewContent(vp);
    			   voca.setGid(vp.getGid());
    			   voca.setCheck(false);
    			   return voca;
    		   }
    		   else {
    			   String message = "No voca match with id: " + id;
    			   throw new NotFoundException(message);
    		   }
    	   }
       }
       else {
    	   VocaPreview vp = ofy().load().type(VocaPreview.class).id(id).now();
		   if(vp != null) {
			   Voca voca = new Voca();
			   voca.getVocaPreviewContent(vp);
			   voca.setGid(vp.getGid());
			   voca.setCheck(false);
			   return voca;
		   }
		   else {
			   Voca v = ofy().load().type(Voca.class).id(id).now();
			   if(v != null) {
				   v.setCheck(true);
	    		   return v;
			   }
			   else {
				   String message = "No voca match with id: " + id;
    			   throw new NotFoundException(message);
			   }
		   }
       }
    }
    
    /**
     * Find Voca by Q
     * @param q
     * @param orderSearch <b>true</b> Voca -> VocaPreview, <b>false</b> VocaPreview -> Voca
     * @return
     * @throws NotFoundException
     */
    @ApiMethod(name = "findVocaByQ", path="find_voca_byQ")
    public Voca findVocaByQ(@Named("q") String q, @Named("orderSearch") Boolean orderSearch) throws NotFoundException {
       if(orderSearch) {
    	   Voca v = ofy().load().type(Voca.class).filter("q", q).first().now();
    	   if(v != null) {
    		   v.setCheck(true);
    		   return v;
    	   }
    	   else {
    		   VocaPreview vp = ofy().load().type(VocaPreview.class).filter("q", q).first().now();
    		   if(vp != null) {
    			   Voca voca = new Voca();
    			   voca.getVocaPreviewContent(vp);
    			   voca.setGid(vp.getGid());
    			   voca.setCheck(false);
    			   return voca;
    		   }
    		   else {
    			   String message = q + " not found";
    			   throw new NotFoundException(message);
    		   }
    	   }
       }
       else {
    	   VocaPreview vp = ofy().load().type(VocaPreview.class).filter("q", q).first().now();
		   if(vp != null) {
			   Voca voca = new Voca();
			   voca.getVocaPreviewContent(vp);
			   voca.setGid(vp.getGid());
			   voca.setCheck(false);
			   return voca;
		   }
		   else {
			   Voca v = ofy().load().type(Voca.class).filter("q", q).first().now();
			   if(v != null) {
				   v.setCheck(true);
	    		   return v;
			   }
			   else {
				   String message = q + " not found";
    			   throw new NotFoundException(message);
			   }
		   }
       }
    }
    
    /**
     * Open upload url for app
     * @return
     */
    @ApiMethod(name = "getUploadUrl")
    public UploadTarget getUploadUrl() {
    	UploadTarget ut = new UploadTarget();
    	ut.setUrl(blobStoreService.createUploadUrl("/photo_upload"));
    	return ut;
    }

}
