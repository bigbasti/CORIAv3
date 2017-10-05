package com.bigbasti.coria.controller;

import com.bigbasti.coria.db.DataStorage;
import com.bigbasti.coria.export.ExportAdapter;
import com.bigbasti.coria.parser.InputParser;
import com.fasterxml.jackson.databind.ser.Serializers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * Created by Sebastian Gross
 */
@Controller
@RequestMapping(path = "/api/modules")
public class ModulesController extends BaseController {
    private Logger logger = LoggerFactory.getLogger(ModulesController.class);

    /**
     * get all registered import providers
     * @return
     */
    @GetMapping(path = "/import")
    public @ResponseBody List<InputParser> getAllImportProviders(){
        logger.debug("retrieving all input providers");
        return availableInputParsers;
    }

    /**
     * get all registered export providers
     * @return
     */
    @GetMapping(path = "/export")
    public @ResponseBody List<ExportAdapter> getAllExportProviders(){
        logger.debug("retrieving all export providers");
        return exportAdapters;
    }

    /**
     * get all registered metrics providers
     * @return
     */
    @GetMapping(path = "/metrics")
    public @ResponseBody
    ResponseEntity getAllMetrics() {
        logger.debug("retrieving all available metrics");
        return ResponseEntity.ok(metrics);
    }

    /**
     * get all registered storage providers
     * @return
     */
    @GetMapping(path = "/storage")
    public @ResponseBody
    ResponseEntity getAllStorageadapters() {
        logger.debug("retrieving all available storage adapters");
        return ResponseEntity.ok(dataStorages);
    }
}
