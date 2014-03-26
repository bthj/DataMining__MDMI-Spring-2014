package is.bthj.itu.datamining.preprocessing.enums;

public enum ProgrammingLanguages {

	CSharp("C#", "C Sharp"),
	Java("Java"),
	Python("Python"),
	CPlusPlus("C++"),
	JavaScript("JavaScript", "JS"),
	R("R"),
	ActionScript("ActionScript"),
	PHP("PHP"),
	C("C"),
	Clojure("Clojure"),
	Ruby("Ruby"),
	Delphi("Delphi"),
	FSharp("F#", "F Sharp"),
	Groovy("Groovy"),
	ObjectiveC("ObjectiveC", "Objective C", "Obj-C"),
	CommonLisp("Common Lisp", "Lisp"),
	Perl("Perl"),
	Bash("Bash"),
	Scala("Scala"),
	MoscowML("Moscow ML", "mosml"),
	Go("Go"),
	Matlab("Matlab"),
	XSharp("X#");
	
	
	private String[] programmingLanguages;
	
	private ProgrammingLanguages( String... language ) {
		this.programmingLanguages = language;
	}
	
	
	public static ProgrammingLanguages getProgrammingLanguage( String givenLang ) {
		ProgrammingLanguages returnedProgrammingLanguage = null;
		
		givenLang = givenLang.toLowerCase().trim();
		
		enumLoop:
		for( ProgrammingLanguages oneProgLang : ProgrammingLanguages.values() ) {
			for( String oneProgWriting : oneProgLang.programmingLanguages ) {
				
				if( givenLang.equals(oneProgWriting.toLowerCase()) ) {
					returnedProgrammingLanguage = oneProgLang;
					break enumLoop;
				}
			}
		}
		
		return returnedProgrammingLanguage;
	}
}
