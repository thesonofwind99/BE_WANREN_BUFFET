// src/main/java/com/fpoly/be_wanren_buffet/rest/CustomerRestController.java

package com.fpoly.be_wanren_buffet.rest;

import com.fpoly.be_wanren_buffet.dao.TableRepository;
import com.fpoly.be_wanren_buffet.entity.Tablee;
import com.fpoly.be_wanren_buffet.enums.TableStatus;
import com.fpoly.be_wanren_buffet.service.TableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/table")
public class TableRestController {
    @Autowired
    private TableService tableService;
    @PutMapping("/{tableId}")
    public ResponseEntity<Tablee> updateTableStatus(@PathVariable Long tableId, @RequestBody TableStatusRequest request) {
        Tablee updatedTable = tableService.updateTableStatus(tableId, request.getTableStatus());
        return ResponseEntity.ok(updatedTable);
    }

    public static class TableStatusRequest {
        private TableStatus tableStatus;

        public TableStatus getTableStatus() {
            return tableStatus;
        }

        public void setTableStatus(TableStatus tableStatus) {
            this.tableStatus = tableStatus;
        }
    }
}
