package com.bigbasti.coria.controller;

import com.bigbasti.coria.config.AppContext;
import com.bigbasti.coria.dataset.DataSet;
import com.bigbasti.coria.db.DataStorage;
import com.bigbasti.coria.graph.CoriaEdge;
import com.bigbasti.coria.graph.CoriaNode;
import com.bigbasti.coria.model.DataSetUpload;
import com.bigbasti.coria.parser.FormatNotSupportedException;
import com.bigbasti.coria.parser.InputParser;
import com.sun.xml.internal.ws.encoding.DataHandlerDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Sebastian Gross
 */
@Controller
@RequestMapping(path = "/api/datasets")
public class DatasetController {
    private Logger logger = LoggerFactory.getLogger(DatasetController.class);

    @Autowired
    List<InputParser> availableInputParsers;

    @Autowired
    List<DataStorage> dataStorages;

    @RequestMapping(value = {"", "/"}, method = RequestMethod.GET)
    public String getFullDataSets(){
        logger.debug("retrieving full dataset list");
        DataStorage storage = getActiveStorage();

        return "forward:/index.html";
    }

    @GetMapping(path = "/short")
    public String getShortDataSets(){
        logger.debug("retrieving short dataset list");
        DataStorage storage = getActiveStorage();

        return "forward:/index.html";
    }

    @PostMapping("/upload")
    public @ResponseBody
    ResponseEntity handleDataSetUpload(DataSetUpload upload) {
        logger.debug("received dataset file for import");

        if(upload.isValid()){
            InputParser parser = getInputParser(upload.getParser());

            try {
                Object result = parser.getParsedObjects(upload.getFile().getBytes());
                if(result == null){
                    //iser specified an unknown parser
                    logger.error("the parser " + upload.getParser() + " is unknown to coria");
                    return ResponseEntity.status(500).body("{\"error\":\"The specified parser is not registered in CORIA\"}");
                }
                ArrayList<CoriaEdge> edges = null;
                ArrayList<CoriaNode> nodes = null;

                try{
                    edges = (ArrayList<CoriaEdge>) result;
                    logger.debug("successfully parsed " + edges.size() + " edges from upload");
                    if(edges.size() == 0){
                        logger.debug("no edges was created by the import - there must be something wrong with the data file");
                        return ResponseEntity.status(500).body("{\"error\":\"no data was created by the import - there must be something wrong with the data file\"}");
                    }

                    DataSet dataSet = new DataSet();
                    dataSet.setCreated(new Date());
                    dataSet.setEdges(edges);
                    dataSet.setName(upload.getName());
                    getActiveStorage().addDataSet(dataSet);

                    logger.debug("new dataset successfully stored");
                    return ResponseEntity.ok(dataSet);
                }catch (Exception ex){
                    logger.error("Saving Edges failed: {}", ex.getMessage());
                }

                try{
                    nodes = (ArrayList<CoriaNode>) result;
                    logger.debug("successfully parsed " + edges.size() + " nodes from upload");
                    if(nodes.size() == 0){
                        logger.debug("no edges was created by the import - there must be something wrong with the data file");
                        return ResponseEntity.status(500).body("{\"error\":\"no data was created by the import - there must be something wrong with the data file\"}");
                    }

                    DataSet dataSet = new DataSet();
                    dataSet.setCreated(new Date());
                    dataSet.setNodes(nodes);
                    dataSet.setName(upload.getName());
                    getActiveStorage().addDataSet(dataSet);

                    logger.debug("new dataset successfully stored");
                    return ResponseEntity.ok(dataSet);
                }catch (Exception ex){
                    logger.error("Saving Nodes failed: {}", ex.getMessage());
                    return ResponseEntity.status(500).body("{\"error\":\"Please check if the file has the appropriate format (" + ex.getMessage() + "\"}");
                }
                //if we come this far there is something wring with the upload/parser
//                return ResponseEntity.status(500).body("{\"error\":\"Please check if the file has the appropriate format\"}");

            } catch (FormatNotSupportedException e) {
                return ResponseEntity.status(500).body("{\"error\":\"The uploaded file does not match the specified format\"}");
            } catch (IOException e) {
                return ResponseEntity.status(500).body("{\"error\":\"Error while processing the uploaded file: " + e.getMessage() + "\"}");
            }
        }else{
            logger.error("received an invalid upload");
            return ResponseEntity.status(500).body("{\"error\":\"Please make sure all required fields are provided\"}");
        }
    }

    private InputParser getInputParser(String id){
        InputParser parser = availableInputParsers
                .stream()
                .filter(inputParser -> inputParser.getIdentification().equals(id))
                .findFirst()
                .get();
        logger.debug("using following inputParser: " + parser);
        return parser;
    }

    private DataStorage getActiveStorage(){
        String targetDb = AppContext.getInstance().getDatabaseProvider();
        DataStorage storage = dataStorages
                .stream()
                .filter(dataStorage -> dataStorage.getIdentification().equals(targetDb))
                .findFirst()
                .get();
        logger.debug("using dataStorage: " + storage);
        return storage;
    }
}
