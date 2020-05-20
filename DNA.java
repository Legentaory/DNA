/**
 * @author ${Wentao Li}
 *
 * @version ${1.0}
 */

import edu.duke.*;

public class DNA {
    private String DNAStrand;

    DNA(String DNAStrand) {
        this.DNAStrand = DNAStrand;
    }

    public String getDNAStrand() {
        return DNAStrand;
    }

    public void setDNAStrand(String DNAStrand) {
        this.DNAStrand = DNAStrand;
    }

    /**
     * @return StorageResource type
     */
    public StorageResource getGene(){
        String DNAString = DNAStrand;
        DNAString = DNAString.toUpperCase();
        StorageResource sr = new StorageResource();
        if(DNAString == null||DNAString.isEmpty()){
            try {
                throw new Exception("No inputs!");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        String gene = "";
        int ATGIndex = 0;
        int TAAIndex = 0;
        int TAGIndex = 0;
        int TGAIndex = 0;
        int stopCodon = 0;
        int count = 0;
        ATGIndex = DNAString.indexOf("ATG");
        int CGRtiaoCount=0;
        int L60Count = 0;
        int CTGCount = 0;
        while (ATGIndex!=-1){
            int condon = 0;
            TAAIndex = getCodonTarget(DNAString,ATGIndex,"TAA");
            TAGIndex = getCodonTarget(DNAString,ATGIndex,"TAG");
            TGAIndex = getCodonTarget(DNAString,ATGIndex,"TGA");
            stopCodon = getStopCodon(TAAIndex,TAGIndex,TGAIndex);
            if (stopCodon != -1) {
                gene = DNAString.substring(ATGIndex,stopCodon+3);
                count ++;
                System.out.println("Gene " + count + "with length" + gene.length() + ": " + gene);
                sr.add(gene);
                ATGIndex = DNAString.indexOf("ATG",stopCodon+3);
            }

            else{ATGIndex = DNAString.indexOf("ATG",ATGIndex+3);}
        }

        return sr;
    }

    /**
     *
     * @param gene
     * @return Gene's info stored
     */
    public StorageResource additionalInfo(StorageResource gene){
        StorageResource st = new StorageResource();
        int maxLen = 0;
        int totalCGOver35 = 0;
        int totalCTG = 0;
        for(String s:gene.data()){
            maxLen = Math.max(maxLen,s.length());

            StringBuilder sb = new StringBuilder();
            sb.append("The CG percentage is: ");
            int CGcount = CGcounter(s);
            if((double)CGcount/s.length()>0.35){
                totalCGOver35++;
            }
            sb.append((double) CGcount/ s.length());
            sb.append(" CTG numbers: ");

            int CTGcount = codonCount("CTG",s);
            totalCTG = totalCTG + CTGcount;
            sb.append(CTGcount);
            sb.append(" Gene length: ");
            sb.append(s.length());
            st.add(sb.toString());
        }
        StringBuilder sb = new StringBuilder();
        sb.append("The maximum gene length: ");
        sb.append(maxLen);
        sb.append(" ,The counts for CG percentage over 0.35: ");
        sb.append(totalCGOver35);
        sb.append(" ,The total counts for CTG: ");
        sb.append(totalCTG);
        System.out.println(sb.toString());
        st.add(sb.toString());
        return st;
    }

    private int getCodonTarget(String DNAString,int ATGIndex,String targetString){
        int returnIndex = DNAString.indexOf(targetString,ATGIndex);
        while(returnIndex!=-1&&(returnIndex-ATGIndex)%3!=0){
            returnIndex = DNAString.indexOf(targetString,returnIndex+3);
        }
        return returnIndex;
    }

    private int getStopCodon(int TAAIndex,int TAGIndex,int TGAIndex){
        if(TAAIndex*TAGIndex*TGAIndex==-1){
            return -1;
        }
        int maxIndex = Math.max(TAAIndex,TAGIndex);
        int minIndex = 0;
        maxIndex = Math.max(TGAIndex,maxIndex);
        minIndex = Math.min(negConvert(TAAIndex,maxIndex),negConvert(TAGIndex,maxIndex));
        minIndex = Math.min(negConvert(TGAIndex,maxIndex),minIndex);
        return minIndex;
    }

    private int CGcounter(String gene){
        gene = gene.toUpperCase();
        int i = 0;
        for(int j = 0; j<gene.length();j++){
            if (gene.charAt(j)=='C'||gene.charAt(j)=='G'){
                i++;
            }
        }
        return i;
    }

    private int codonCount(String targetCodon,String gene){
        gene = gene.toUpperCase();
        int count = 0;
        int index = 0;
        while(index!=-1){
            index = gene.indexOf(targetCodon,index+3);
            count++;
        }
        return count;
    }



    private int negConvert(int a,int max){
        if(a>0){
            return a;
        }
        else{
            return max;
        }
    }
}
