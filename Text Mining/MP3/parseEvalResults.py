from __future__ import division
import csv
with open('evalStatstimingl5.txt', 'w') as the_file:
    for i in range(10):
        knnTP = 0
        knnFP = 0
        knnTN = 0
        knnFN = 0
        bayesTP = 0
        bayesFP = 0
        bayesTN = 0
        bayesFN = 0

        with open('evalResultsTestSet' + str(i) + 'timingl5.txt', 'rb') as csvfile:
            count = 0
            spamreader = csv.reader(csvfile, delimiter=',')
            spamreader.next()
            spamreader.next()
            for row in spamreader:
                count += 1
                if row[0] == '0':
                    if row[1] == '0':
                        bayesTN += 1
                    else:
                        bayesFP += 1
                    if row[2] =='0':
                        knnTN += 1
                    else:
                        knnFP += 1
                if row[0] == '1':
                    if row[1] == '0':
                        bayesFN += 1
                    else:
                        bayesTP += 1
                    if row[2] == '0':
                        knnFN += 1
                    else:
                        knnTP += 1
                        
            bayesR = bayesTP/(bayesTP + bayesFN)
            bayesP= bayesTP/(bayesTP + bayesFP)
            knnR = knnTP/(knnTP + knnFN)
            knnP = knnTP/(knnTP + knnFP)
            the_file.write('Statistics for fold: ' + str(i) + '\n')
            the_file.write('Total Reviews: ' + str(count) + '\n')
            the_file.write('BAYES TP: ' + str(bayesTP) + '\n')
            the_file.write('BAYES FP: ' + str(bayesFP) + '\n')
            the_file.write('BAYES TN: ' + str(bayesTN) + '\n')
            the_file.write('BAYES FN: ' + str(bayesFN) + '\n')
            the_file.write('knn TP: ' + str(knnTP) + '\n')
            the_file.write('knn FP: ' + str(knnFP) + '\n')
            the_file.write('knn TN: ' + str(knnTN) + '\n')
            the_file.write('knn FN: ' + str(knnFN) + '\n')

            the_file.write('BAYES RECALL: ' + str(bayesR) + '\n')
            the_file.write('BAYES PRECISION: ' + str(bayesP) + '\n')
            the_file.write('BAYES F1: ' + str(2/((1/bayesP) + (1/bayesR))) + '\n')
            the_file.write('knn RECALL: ' + str(knnR) + '\n')
            the_file.write('knn PRECISION: ' + str(knnP) + '\n')
            the_file.write('knn F1: ' + str(2/((1/knnP) + (1/knnR))) + '\n')
            the_file.write('\n')
