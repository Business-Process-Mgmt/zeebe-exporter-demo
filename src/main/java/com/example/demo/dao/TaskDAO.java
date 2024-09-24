package com.example.demo.dao;

import com.example.demo.domain.ExporterTask;

import java.sql.*;

public class TaskDAO {
    public static void insertTask(Connection conn, ExporterTask task) {
        String checkSql = "SELECT COUNT(*) FROM [dbo].[TASK] WHERE [task_id] = ?";
        String insertSql = "INSERT INTO [dbo].[TASK] " +
                "([task_id], [process_instance_key], [name], [task_definition_id], [creation_date], " +
                "[completed_date], [task_state], [assignee_user], [candidate_groups], [due_date], " +
                "[assigneee_user_fullname], [completed_by_user], [completed_by_user_fullname], " +
                "[child_process_instance_key], [process_id], [task_unique_id], [details], " +
                "[created_by], [created_date], [modified_by], [modified_date]) " +
                "VALUES (?, ?, ?, ?,    ?, ?, ?, ?,    ?, ?, ?, ? ,   ?, ?, ?, ?,   ?, ?, ?, ?,?)";

        try (PreparedStatement checkStmt = conn.prepareStatement(checkSql);
             PreparedStatement insertStmt = conn.prepareStatement(insertSql)
             ) {
            checkStmt.setLong(1, task.getTaskId());
            ResultSet rs = checkStmt.executeQuery();
            rs.next();

            System.out.println("task creation date::"+task.getCreationDate());
            int count = rs.getInt(1);
            if (count == 0) { // If task_id does not exist
                insertStmt.setLong(1, task.getTaskId());
                insertStmt.setLong(2, task.getProcessInstanceKey());
                insertStmt.setString(3, task.getName());
                insertStmt.setString(4, task.getTaskDefinitionId());
                insertStmt.setTimestamp(5, Timestamp.valueOf(task.getCreationDate().atStartOfDay()));//  Task Creation Date
                insertStmt.setTimestamp(6, null); // For completed_date, set to null if not applicable
                insertStmt.setString(7, task.getTaskState());
                insertStmt.setString(8, task.getAssigneeUser());
                insertStmt.setString(9, task.getCandidateGroups());
                insertStmt.setTimestamp(10, null);
                insertStmt.setString(11, task.getAssigneeUserFullName());
                insertStmt.setString(12, null); // For completed_by_user, set to null if not applicable
                insertStmt.setString(13, null); // For completed_by_user_fullname, set to null if not applicable
                insertStmt.setLong(14, 0l);
                insertStmt.setLong(15, 0l);
                insertStmt.setString(16, task.getTaskUniqueId());
                insertStmt.setString(17, task.getDetails());
                insertStmt.setString(18, task.getCreatedBy());
                insertStmt.setTimestamp(19, null);
                insertStmt.setString(20, null); // For modified_by, set to null if not applicable
                insertStmt.setTimestamp(21, null); // For modified_date, set to null if not applicable

                int rowsInserted = insertStmt.executeUpdate();
                if (rowsInserted > 0) {
                    System.out.println("A new task was inserted successfully!");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
