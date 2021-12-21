/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later.
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.envers.bugs;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.cfg.AvailableSettings;
import org.hibernate.cfg.Configuration;
import org.hibernate.envers.AuditReader;
import org.hibernate.testing.TestForIssue;
import org.junit.Test;

/**
 * This template demonstrates how to develop a test case for Hibernate Envers, using
 * its built-in unit test framework.
 */
public class EnversUnitTestCase extends AbstractEnversTestCase {

    // Add your entities here.
    @Override
    protected Class[] getAnnotatedClasses() {
        return new Class[]{
                Book.class,
                Author.class,
				Publisher.class
        };
    }

    // Add in any settings that are specific to your test.  See resources/hibernate.properties for the defaults.
    @Override
    protected void configure(Configuration configuration) {
        super.configure(configuration);

        configuration.setProperty(AvailableSettings.SHOW_SQL, Boolean.TRUE.toString());
        configuration.setProperty(AvailableSettings.FORMAT_SQL, Boolean.TRUE.toString());
        //configuration.setProperty( AvailableSettings.GENERATE_STATISTICS, "true" );
    }

    @Test
    public void manyToManyByReferenceSimpleKey_works() throws Exception {
		Long authorId;
        try (Session s = openSession()) {
			Author author = new Author();
			author.setName("PJ");
			Transaction tx = s.beginTransaction();
			s.persist( author );
			tx.commit();
			authorId = author.getId();
        }

		try (Session s = openSession()) {
			Book book = new Book();
			book.setTitle("PJ's Life");
			book.getAuthors().add(s.getReference(Author.class, authorId));

			Transaction tx = s.beginTransaction();
			s.persist( book );
			tx.commit();
		}
    }

	@Test
	public void manyToManyCompositeKey_works() throws Exception {
		Publisher.PublisherId publisherId;
		try (Session s = openSession()) {
			Publisher publisher = new Publisher();
			publisherId = new Publisher.PublisherId("EBOOK", "REPRINTBOOKS");
			publisher.setId(publisherId);
			Transaction tx = s.beginTransaction();
			s.persist( publisher );
			tx.commit();
		}

		try (Session s = openSession()) {
			Book book = new Book();
			book.setTitle("PJ's Life");
			book.getPublisher().add(s.get(Publisher.class, publisherId));

			Transaction tx = s.beginTransaction();
			s.persist( book );
			tx.commit();
		}
	}

	@TestForIssue(jiraKey = "HHH-14994")
	@Test
	public void manyToManyByReferenceCompositeKey_fails() throws Exception {
		Publisher.PublisherId publisherId;
		try (Session s = openSession()) {
			Publisher publisher = new Publisher();
			publisherId = new Publisher.PublisherId("EBOOK", "REPRINTBOOKS");
			publisher.setId(publisherId);
			Transaction tx = s.beginTransaction();
			s.persist( publisher );
			tx.commit();
		}

		try (Session s = openSession()) {
			Book book = new Book();
			book.setTitle("PJ's Life");
			book.getPublisher().add(s.getReference(Publisher.class, publisherId));

			Transaction tx = s.beginTransaction();
			s.persist( book );
			tx.commit();
		}
	}
}
