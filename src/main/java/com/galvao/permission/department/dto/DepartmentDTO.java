package com.galvao.permission.department.dto;

import java.util.List;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
@AllArgsConstructor
public class DepartmentDTO {
	private UUID id;
	private String name;
	private DepartmentDTO parent;
	private List<DepartmentDTO> children;
}
