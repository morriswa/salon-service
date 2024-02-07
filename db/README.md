
# Salon MySQL Instance 
## Current DB Version: 1.2
#### Upgrade to V1 from New Schema (when run from project directory)
    mysql -u dev_dynasty_service -p dev_dynasty_salon < ./db/migration/V1__CreateUserAccountTable.sql
#### V1 -> V1.1
    mysql -u dev_dynasty_service -p dev_dynasty_salon < ./db/migration/V1.1__CreateContactInfoTable.sql
#### V1.1 -> V1.2
    mysql -u dev_dynasty_service -p dev_dynasty_salon < ./db/migration/V1.2__AddRoleOnUserAccount.sql

If you run into DAO errors, please ensure your database schema matches the current version.

If you are unable to migrate to the desired version, try running the scripts in backward_migration 
and re-running desired migration script.

If you are unable to troubleshoot issues encountered during migration, run the ResetDatabase script
followed by ALL MIGRATION SCRIPTS IN ORDER (V1 -> V1.1 -> V1.2 etc).

All migration scripts should be tested to ensure correctness before running on 
EECS MySQL instance. Since this will serve as our "Production" deployment, troubleshooting directly 
on this instance should be AVOIDED AT ALL COSTS.

## Important Commands
### Launch Root Console
    mysql -u root -p
### Run sql script
    mysql -u [username] -p [database] < [path to script]
