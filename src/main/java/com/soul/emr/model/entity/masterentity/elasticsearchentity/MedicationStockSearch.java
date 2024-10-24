package com.soul.emr.model.entity.masterentity.elasticsearchentity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.soul.emr.model.entity.commonentity.WhoseColumnsEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Field;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;

@EqualsAndHashCode(callSuper = false)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MedicationStockSearch extends WhoseColumnsEntity implements Serializable
{
	@Serial
	private static final long serialVersionUID = 1L;
	
	@Id
	@JsonProperty("medicationStockId")
	@Field(name = "medication_Stock_Id")
	private Long medicationStockId;
	
	@JsonProperty("batchCode")
	@Field(name = "batch_code")
	private String batchCode;
	
	@JsonProperty("batchDate")
	@Field(name = "batch_date")
	@JsonSerialize(using = LocalDateSerializer.class)
	@JsonDeserialize(using = LocalDateDeserializer.class)
	@JsonFormat(shape = JsonFormat.Shape.STRING)
	private LocalDate batchDate;
	
	@JsonProperty("expiryDate")
	@Field(name = "expiry_Date")
	@JsonSerialize(using = LocalDateSerializer.class)
	@JsonDeserialize(using = LocalDateDeserializer.class)
	@JsonFormat(shape = JsonFormat.Shape.STRING)
	private LocalDate expiryDate;
	
	@JsonProperty("storeId")
	@Field(name = "store_id")
	private Long storeId;
	
	@JsonProperty("stock")
	@Field(name = "stock")
	private Float stock;
	
	@JsonProperty("price")
	@Field(name = "price")
	private Double price;
	
	@JsonProperty("cgst")
	@Field(name = "cgst")
	private Double cgst;
	
	@JsonProperty("sgst")
	@Field(name = "sgst")
	private Double sgst;
}
