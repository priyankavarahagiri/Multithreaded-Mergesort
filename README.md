# Multithreaded-Mergesort
A collection of items is divided into two lists of equal size. Each list is then passed to a separate thread which sorts the lists using any sorting algorithm.
The two sorted lists are passed to a third thread(merge thread) which merges the two separate lists into a single sorted list. Once the two lists are merged, the complete sorted list is the output.
