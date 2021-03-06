package com.pengurus.crm.server.services;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.security.access.prepost.PreAuthorize;

import com.pengurus.crm.client.service.JobService;
import com.pengurus.crm.client.service.exceptions.DeleteException;
import com.pengurus.crm.daos.JobDAO;
import com.pengurus.crm.daos.QuoteDAO;
import com.pengurus.crm.daos.TranslationDAO;
import com.pengurus.crm.entities.Job;
import com.pengurus.crm.entities.Quote;
import com.pengurus.crm.enums.StatusJob;
import com.pengurus.crm.enums.StatusQuote;
import com.pengurus.crm.shared.dto.JobDTO;

public class JobServiceImpl implements JobService {

	private QuoteDAO quoteDAO;
	private JobDAO jobDAO;
	private TranslationDAO translationDAO;
	
	public JobDAO getJobDAO() {
		return jobDAO;
	}

	public void setJobDAO(JobDAO jobDAO) {
		this.jobDAO = jobDAO;
	}

	public TranslationDAO getTranslationDAO() {
		return translationDAO;
	}

	public void setTranslationDAO(TranslationDAO translationDAO) {
		this.translationDAO = translationDAO;
	}

	@Override
	@PreAuthorize("hasRole('ROLE_EXECUTIVE')")
	public JobDTO createJob(JobDTO jobDTO) {
		jobDTO.setId(jobDAO.create(new Job(jobDTO)));
		return jobDTO;
	}

	@Override
	@PreAuthorize("hasAnyRole('ROLE_EXECUTIVE', 'ROLE_PROJECTMANAGER', 'ROLE_CLIENT')")
	public JobDTO getJob(Long id) {
		return jobDAO.getById(id).toDTO();
	}

	@Override
	@PreAuthorize("hasRole('ROLE_EXECUTIVE')")
	public JobDTO updateJob(JobDTO jobDTO) {
		Job j = new Job(jobDTO);
		jobDAO.update(j);
		return j.toDTO();
	}

	@Override
	@PreAuthorize("hasRole('ROLE_WORKER')")
	public Set<JobDTO> getJobByExpertId(Long id) {
		List<Job> list = jobDAO.loadAllByExpertId(id);
		Set<JobDTO> set = new HashSet<JobDTO>();
		for (Job job : list) {
			set.add(job.toDTOLazy());
		}
		return set;
	}

	@Override
	@PreAuthorize("hasRole('ROLE_EXECUTIVE')")
	public void deleteJob(JobDTO jobDTO) throws DeleteException {
		if (!jobDAO.delete(new Job(jobDTO))) {
			throw new DeleteException();
		}

	}

	@Override
	@PreAuthorize("hasAnyRole('ROLE_EXECUTIVE', 'ROLE_PROJECTMANAGER', 'ROLE_CLIENT')")
	public void updateStatus(JobDTO jobDTO) {
		Job job = jobDAO.read(jobDTO.getId());
		job.setStatus(StatusJob.toStatus(jobDTO.getStatus()));
		Quote quote = quoteDAO.loadByJobId(jobDTO.getId());
		StatusJob status = StatusJob.closed;
		for (Job j : quote.getJobs()) {
			if (status.stage() < j.getStatus().stage()) {
				status = j.getStatus();
			}
		}
		if (status == StatusJob.accepted) {
			quote.setStatus(StatusQuote.accepted);
			quoteDAO.update(quote);
		} else if (status == StatusJob.accounted) {
			quote.setStatus(StatusQuote.accounted);
			quoteDAO.update(quote);
		}
		jobDAO.update(job);
	}

	public void setQuoteDAO(QuoteDAO quoteDAO) {
		this.quoteDAO = quoteDAO;
	}

	public QuoteDAO getQuoteDAO() {
		return quoteDAO;
	}
}
