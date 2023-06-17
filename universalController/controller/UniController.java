package universalController.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.google.gson.Gson;
import CSVImport.CSVProcessor;
import gdao.genericdao.GenericDAO;
import gdao.inherit.DBModel;
import gdao.login.token.Token;
import pdf.PdfGen;
import qc.QC;
import qc.query.Query;
import qc.search.SearchParameters;
import requestHandler.Updatinator;
import responseHandler.Error;
import responseHandler.Failure;
import responseHandler.Response;
import responseHandler.Success;
import universalController.classmanaging.Classe;

@RestController
@CrossOrigin
public class UniController<T extends DBModel> {
    @GetMapping("/info")
    public Response getInfo(T model) throws Exception {
        return new Success(Classe.translate(model.getClass()));
    }

    @GetMapping("/initCRUD")
    public Response getInfoCRUD(T model) throws Exception {
        HashMap<String, Object> res = new HashMap<>();
        res.put("infoClasse", Classe.translate(model.getClass()));
        res.put("liste", model.listAll().size());
        System.gc();
        return new Success(res);
    }

    @GetMapping()
    public Response get(T model) throws Exception {
        try {
            // System.out.println(model.list());
            System.gc();
            return new Success(model.listAll());
        } catch (Exception e) {
            return new Failure(new Error(500, e.getMessage()));
        }
    }

    @GetMapping("/pdf")
    public Response pdfAll(T model) throws Exception {
        try {
            System.gc();
            ArrayList<T> list = model.listAll();
            PdfGen.genPdfLst(model.getClass().getSimpleName() + ".pdf", (ArrayList) list);
            // System.out.println(model.list());
            System.gc();
            return new Success(model.getClass().getSimpleName() + ".pdf");
        } catch (Exception e) {
            return new Failure(new Error(500, e.getMessage()));
        }
    }

    @GetMapping("/pdf/{ind}")
    public Response pdfAll(T model, @PathVariable("ind") Integer id) throws Exception {
        try {
            model.setPkVal(id);
            ArrayList<T> list = model.list();
            PdfGen.genPdf(model.getClass().getSimpleName() + ".pdf", list.get(0));
            // System.out.println(model.list());
            System.gc();
            return new Success(model.getClass().getSimpleName() + ".pdf");
        } catch (Exception e) {
            return new Failure(new Error(500, e.getMessage()));
        }
    }

    @GetMapping("/{ind}")
    public Response get1(T model, @PathVariable("ind") Integer id) throws Exception {
        try {
            // System.out.println(model.list());
            model.setPkVal(id);
            System.gc();
            return new Success(model.list());
        } catch (Exception e) {
            return new Failure(new Error(500, e.getMessage()));
        }
    }

    @PostMapping("/search")
    public Response search(@RequestBody SearchParameters sp, T model) throws Exception {
        try {
            // String name=4
            System.gc();
            System.out.println(sp);
            Query query = QC.buildMultiQuery(sp, GenericDAO.checkSelectTableName(model, ""));
            String q = query.getPrepdQuery();
            System.out.println(q);
            System.out.println(new Gson().toJson(query.getPrepdQueryObject()));
            return new Success(GenericDAO.executeQuery(q, query.getPrepdQueryObject(), model, null));
        } catch (Exception e) {
            e.printStackTrace();
            return new Failure(new Error(500, e.getMessage()));
        }
    }

    // @GetMapping("test")
    // public Response get2(@RequestParam("ref") String jsonObject, T model) throws
    // Exception{
    // Gson gson = new Gson();
    // model=(T) gson.fromJson(jsonObject, model.getClass());
    // try {
    // // System.out.println(model.list());
    // return new Success(model.list());
    // } catch (Exception e) {
    // return new Failure(new Error(500, e.getMessage()));
    // }
    // }
    @PostMapping
    public Response create(@RequestBody T model, @RequestHeader(name = "token") String token) throws Exception {
        try {
            // System.out.println(model);
            if (Token.isTokenAvailable(token)) {
                model.save();
                System.gc();
                return new Success(model.list());
            } else {
                System.out.println("token expired");
                return new Failure(new Error(403, "token expired"));
            }
            // return null;
        } catch (Exception e) {
            e.printStackTrace();
            return new Failure(new Error(500, e.getMessage()));
        }
    }

    @PostMapping("/CSV/upload")
    public Response csvUpload(@RequestParam MultipartFile file, @RequestHeader(name = "token")  String token,
            T model) throws Exception {
        try {
            // System.out.println(model);
            if (Token.isTokenAvailable(token)) {
                System.out.println(file);
                List<T> list = CSVProcessor.readCSV(file, model.getClass());
                System.gc();
                return new Success(model.saveList(list));
            } else {
                System.out.println("token expired");
                return new Failure(new Error(403, "token expired"));
            }
            // return null;
        } catch (Exception e) {
            e.printStackTrace();
            return new Failure(new Error(500, e.getMessage()));
        }
    }

    @DeleteMapping()
    public Response delete(@RequestBody T model, @RequestHeader(name = "token") String token) throws Exception {
        try {
            if (!Token.isTokenAvailable(token))
                return new Failure(new Error(403, "token expired"));
            // System.out.println(model);
            model.delete();
            System.gc();
            return new Success("réussite");
            // return null;
        } catch (Exception e) {
            e.printStackTrace();
            return new Failure(new Error(500, e.getMessage()));
        }
    }

    @PutMapping
    public Response update(@RequestBody Updatinator<T> update, @RequestHeader(name = "token") String token)
            throws Exception {
        try {
            if (!Token.isTokenAvailable(token))
                return new Failure(new Error(403, "token expired"));
            System.out.println(update);
            update.getOld().update(update.getBrand());
            System.gc();
            return new Success(update.getBrand().list());
            // return null;
        } catch (Exception e) {
            e.printStackTrace();
            return new Failure(new Error(500, e.getMessage()));
        }
    }
}
