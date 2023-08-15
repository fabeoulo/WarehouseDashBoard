/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.advantech.repo;

import com.advantech.model.Floor;
import com.advantech.model.Line;
import com.advantech.model.LineSchedule;
import com.advantech.model.LineScheduleStatus;
import com.advantech.model.RemoteSchedule;
import java.util.Date;
import java.util.List;
import org.springframework.data.jpa.datatables.repository.DataTablesRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Wei.Cheng
 */
@Repository
public interface LineScheduleRepository extends JpaRepository<LineSchedule, Integer>, DataTablesRepository<LineSchedule, Integer> {

    public LineSchedule findFirstByPoAndLineScheduleStatusNot(String po, LineScheduleStatus status);

    public LineSchedule findFirstByPoAndFloorAndLineScheduleStatusNot(String po, Floor f, LineScheduleStatus status);

    @Query("select l from LineSchedule l where l.floor = ?2 and l.po IN ?1 and l.lineScheduleStatus != ?3")
    public List<LineSchedule> findByPosAndFloorAndLineScheduleStatusNot(List<String> pos, Floor f, LineScheduleStatus status);

    @Query(value = "{CALL usp_GetPrepareSchedule(:sD)}", nativeQuery = true)
    public List<RemoteSchedule> getPrepareSchedule(@Param("sD") Date sD);

    public List<LineSchedule> findByLine(Line line);

    public List<LineSchedule> findByOnBoardDateBetween(Date sD, Date eD);

    public List<LineSchedule> findByFloorIdAndOnBoardDateGreaterThan(int floorId, Date sD);

}
