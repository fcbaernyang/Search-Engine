Xian Chen
CS446: Search Engines

Code Files: 
SearchEngine.java 
Term.java

Introduction:
The program constructs an inverted index and ranks documents in a collection using the B25 algorithm (B25 method) or Query-likelihood coupled with Dirichlet smoothing (QL method).

How To Use:
1.	Have tccorpus.txt in the same directory as the .java files. Run the program. It will start by constructing the inverted index. 
2.	The program will now ask for a mu value (used only for Dirchlet smoothing). Enter a mu value. The program will now as for queries in a loop, outputting the top 10 results. You may enter as many queries as you like to the program for this mu value. When you are done, just press “enter” and the program will terminate. All the output result from this session will be written to a file called “output.txt”.  To enter a new mu value, restart the program. 

Notes concerning mu:
“Small values of mu give more importance to the relative weighting of words and large values favor the number of matching terms” (Croft et al 259). Increasing mu too much puts too much importance on how many times a word appears in the collection and as a result, skews the weight of each word and cancel out the effect of the document length. As you can see from the denominator of the equation, which is doc_length + mu, if mu is very small, then the doc_length will have a large effect on the ranking but if mu is very large, then the doc_length is negligible.