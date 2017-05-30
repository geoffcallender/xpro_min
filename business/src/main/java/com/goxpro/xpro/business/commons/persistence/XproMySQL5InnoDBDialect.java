//
// Based on http://seamframework.org/Documentation/UsingMySQLInProductionWithUTF8
//

package com.goxpro.xpro.business.commons.persistence;

import java.sql.Types;

import org.hibernate.dialect.MySQL5InnoDBDialect;

public class XproMySQL5InnoDBDialect extends MySQL5InnoDBDialect {

	public XproMySQL5InnoDBDialect() {
		super();

		// For Hibernate 4 with MySQL 5.5 in two steps:

		// 1. Stop Hibernate mapping SQL BOOLEAN keyword to bit. Make it map to tinyint(1) just like MySQL does.

		registerColumnType(Types.BOOLEAN, "tinyint(1)");

		// 2. Stop Hibernate schema validation failing on booleans (ie. when hibernate.hbm2ddl.auto=validate). The cause
		// is the MySQL connector reporting "tinyint(1)" as "bit" instead of "boolean". The solution is to set the
		// connector property tinyInt1isBit=false. See
		// http://stackoverflow.com/questions/14381749/spring-hibernate-found-bit-expected-tinyint1-default-0 .

	}

	// @Override
	// protected void registerVarcharTypes() {
	// registerColumnType(Types.VARCHAR, "longtext");
	// registerColumnType(Types.VARCHAR, 16777215, "mediumtext");
	// registerColumnType(Types.VARCHAR, 1023, "varchar($l)");
	// }

	// @Override
	// public String getTableTypeString() {
	// return " ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE utf8_general_ci";
	// }

}
