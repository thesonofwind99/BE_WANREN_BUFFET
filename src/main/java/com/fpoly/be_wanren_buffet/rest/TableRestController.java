// src/main/java/com/fpoly/be_wanren_buffet/rest/CustomerRestController.java

package com.fpoly.be_wanren_buffet.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.fpoly.be_wanren_buffet.entity.Tablee;
import com.fpoly.be_wanren_buffet.enums.TableStatus;
import com.fpoly.be_wanren_buffet.service.TableService;

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

    @GetMapping("/status/{tableId}")
    public ResponseEntity<String> getTableStatus(@PathVariable Long tableId) {
        Tablee table = tableService.findTableById(tableId);
        if (table != null) {
            return ResponseEntity.ok(table.getTableStatus().name());
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
