package com.pengurus.crm.hibernate;

import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.pengurus.crm.daos.JobDAO;
import com.pengurus.crm.daos.LanguageDAO;
import com.pengurus.crm.daos.PriceDAO;
import com.pengurus.crm.daos.TaskDAO;
import com.pengurus.crm.daos.TranslationDAO;
import com.pengurus.crm.daos.TranslationTypeDAO;
import com.pengurus.crm.daos.TranslatorDAO;
import com.pengurus.crm.entities.CurrencyType;
import com.pengurus.crm.entities.Job;
import com.pengurus.crm.entities.Language;
import com.pengurus.crm.entities.PersonalData;
import com.pengurus.crm.entities.Price;
import com.pengurus.crm.entities.Task;
import com.pengurus.crm.entities.Translation;
import com.pengurus.crm.entities.TranslationType;
import com.pengurus.crm.entities.Translator;
import com.pengurus.crm.enums.Rating;
import com.pengurus.crm.enums.Status;
import com.pengurus.crm.shared.dto.UserRoleDTO;

@ContextConfiguration(locations = { "../server/testContext.xml" })
@RunWith(SpringJUnit4ClassRunner.class)
public class DatabaseBackupTest {

	@Autowired
	private TranslationDAO translationDAO;
	@Autowired
	private TranslatorDAO translatorDAO;
	@Autowired
	private LanguageDAO languageDAO;
	@Autowired
	private TranslationTypeDAO translationTypeDAO;
	@Autowired
	private PriceDAO PriceDAO;
	@Autowired
	private JobDAO jobDAO;
	@Autowired
	private TaskDAO taskDAO;

	@Before
	public void prepareDatabase() {
		DatabaseBackup.getInstance().refreshDatabase();
	}

	@Test
	public void newEntityAdding() {
		/**
		 * here put code which is responsible for creating new object and saving
		 * it in database. Before creating new scrypt make sure that you did
		 * everything in proper way, however there is always created the backup
		 * version of sql srypt (just in case you, actually DID something wrong)
		 * 
		 * some example below:
		 */

		/*
		 * Language from = new Language("Latin"); languageDAO.create(from);
		 * languageDAO.create(to);
		 * 
		 * TranslationType translationType = new TranslationType(
		 * "5 chapters of Greek Mythology by Edith Hamilton", "pages");
		 * translationTypeDAO.create(translationType);
		 * 
		 * Price price = new Price(12, new CurrencyType("€"));
		 * PriceDAO.create(price);
		 * 
		 * Translation translation = new Translation(translationType, from, to,
		 * price); translationDAO.create(translation);
		 * 
		 * Set<Translation> translations = new HashSet<Translation>();
		 * translations.add(translation);
		 * 
		 * Set<UserRoleDTO> userRoles = new HashSet<UserRoleDTO>();
		 * userRoles.add(UserRoleDTO.ROLE_EXPERT);
		 * userRoles.add(UserRoleDTO.ROLE_VERIFICATOR);
		 * 
		 * Set<String> phoneNumbers = new HashSet<String>();
		 * phoneNumbers.add("1-888-242‑2100");
		 * phoneNumbers.add("3-210-552-2311"); Set<String> emails = new
		 * HashSet<String>(); emails.add("jeremy_smith@yahoo.com");
		 * emails.add("jeremy_smith@gmail.com"); PersonalData personalData = new
		 * PersonalData("Jeremy", "Smith",
		 * "139 Gunnersbury Avenue, London W3 8LG, United Kingdom",
		 * phoneNumbers, emails);
		 * 
		 * Translator translator = new Translator(userRoles, "jSmith",
		 * "unknownPass", "I'm specialist in Italic and Romance languages.",
		 * personalData, translations); translatorDAO.create(translator);
		 * 
		 * Job job = new Job(); jobDAO.create(job);
		 * 
		 * Task task = new Task(Status.accepted, translator, Calendar
		 * .getInstance().getTime(), translation, 100, price,
		 * "chapters from 1 to 3", job, Rating.ok, "Ok."); Task task2 = new
		 * Task(Status.accepted, translator, Calendar .getInstance().getTime(),
		 * translation, 100, price, "chapters 4 and 5", job, Rating.good,
		 * "5th chapter was was magnificent work!");
		 * 
		 * taskDAO.create(task); taskDAO.create(task2);
		 * 
		 * Set<Task> tasks = new HashSet<Task>(); tasks.add(task2);
		 * tasks.add(task); job.setTask(tasks); jobDAO.update(job);
		 */

		/**
		 * notice that when you run empty test, it's working as if you're
		 * restoring the state of database to the one that is saved in current
		 * import.sql srypt warning: You will lose your backup scrypt
		 * (.import.sql) because it will be overwritten by current import.sql
		 */

	}

	@After
	public void createScrypt() {
		DatabaseBackup.getInstance().generateScrypt();
	}
}
