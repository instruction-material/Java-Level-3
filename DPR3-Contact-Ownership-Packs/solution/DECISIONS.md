# Ownership decisions

- Name the fields, invariant and behavior moved into Phone.
- Compare ContactPrinter's old getter dependence with the retained delegating bridge.
- Record the old client's unchanged source and public calls.
- Explain atomic replacement on invalid second-field input and retained leading zeros.
- Compare a single foreign date helper with a composition-based local extension, including conversion/setup cost.
- Give a demonstrated change scenario and removal trigger for the extracted value object.
