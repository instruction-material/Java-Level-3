# Generic Repository and Query Filters

Both CourseRecord and StudentRecord expose a stable ID through Identified. Complete the filtered and sorted queries on Repository<T extends Identified>. Filter in registration order and return a snapshot that the caller cannot modify. Sort a separate copy using the supplied comparator. Keep stored records and their insertion order intact.

Compile from the extracted project folder with javac --release 21 -Xlint:all -Werror *.java. Run java ContractTests and java Main. The demo finds the active large course and one experienced fictional student.
