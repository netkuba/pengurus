package com.pengurus.crm.entities;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.pengurus.crm.enums.StatusJob;
import com.pengurus.crm.shared.dto.JobDTO;
import com.pengurus.crm.shared.dto.TaskDTO;

public class Job {

	private Long id;
	private StatusJob status;
	private Date deadline;
	private Translation translation;
	private Integer amount;
	private Price price;
	private String description;
	private Set<Task> task;

	public Job() {
		super();
	}

	public Job(StatusJob status, Date deadline, Translation translation,
			Integer amount, Price price, String description, Set<Task> task) {
		super();
		this.status = status;
		this.deadline = deadline;
		this.translation = translation;
		this.amount = amount;
		this.price = price;
		this.description = description;
		this.task = task;
	}

	public Job(JobDTO jobDTO) {
		super();
		if (jobDTO != null) {
			this.id = jobDTO.getId();
			if (jobDTO.getStatus() != null)
				this.status = StatusJob.toStatus(jobDTO.getStatus());
			this.deadline = jobDTO.getDeadline();
			this.description = jobDTO.getDescription();
			this.amount = jobDTO.getAmount();
			if (jobDTO.getPrice() != null)
				this.price = new Price(jobDTO.getPrice());
			if (jobDTO.getTranslation() != null)
				this.translation = new Translation(jobDTO.getTranslation());
			if (jobDTO.getTask() != null) {
				this.task = new HashSet<Task>();
				for (TaskDTO t : jobDTO.getTask())
					this.task.add(new Task(t));
			}

		}
	}

	public Job(Long id) {
		this.id = id;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public StatusJob getStatus() {
		return status;
	}

	public void setStatus(StatusJob status) {
		this.status = status;
	}

	public Date getDeadline() {
		return deadline;
	}

	public void setDeadline(Date deadline) {
		this.deadline = deadline;
	}

	public Translation getTranslation() {
		return translation;
	}

	public void setTranslation(Translation translation) {
		this.translation = translation;
	}

	public Integer getAmount() {
		return amount;
	}

	public void setAmount(Integer amount) {
		this.amount = amount;
	}

	public Price getPrice() {
		return price;
	}

	public void setPrice(Price price) {
		this.price = price;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Set<Task> getTask() {
		return task;
	}

	public void setTask(Set<Task> task) {
		this.task = task;
	}

	public JobDTO toDTO() {
		JobDTO jobDTO = this.toDTOLazy();
		if (this.task != null)
			for (Task t : this.task)
				jobDTO.getTask().add(t.toDTO());
		return jobDTO;
	}

	public JobDTO toDTOLazy() {
		JobDTO jobDTO = new JobDTO();
		jobDTO.setAmount(this.amount);
		jobDTO.setDeadline(this.deadline);
		jobDTO.setDescription(this.description);
		jobDTO.setId(this.id);
		if (this.price != null)
			jobDTO.setPrice(price.toDTO());
		if (this.status != null)
			jobDTO.setStatus(this.status.toDTO());
		if (this.translation != null)
			jobDTO.setTranslation(this.translation.toDTO());
		return jobDTO;
	}

}
