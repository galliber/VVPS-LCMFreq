import org.junit.Assert;
import org.junit.Test;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class UnitTests {
    @Test
    public void test_GenerateFilteredLogFile_returnsCorrectString(){
        //Arrange
        String input1 = "text";
        String input2 = "texttexttext";
        String expectedResult1 = "text    ";
        String expectedResult2 = "texttexttext";
        //Act
        String result1 = Main.formatSpacing(input1, 8);
        String result2 = Main.formatSpacing(input2, 8);
        //Assert
        Assert.assertEquals(expectedResult1, result1);
        Assert.assertEquals(expectedResult2, result2);

    }

    @Test
    public void test_getID_CountPairsFromResultFile_returnsCorrectPairs() throws IOException {
        //Arrange
        File input = new File("TextFiles/testResul.txt");
        List<String[]> expectedResult = new ArrayList<>();
        String[] pair1 = new String[2];
        pair1[0] = "982";
        pair1[1] = "708";
        String[] pair2 = new String[2];
        pair2[0] = "761";
        pair2[1] = "709";
        String[] pair3 = new String[2];
        pair3[0] = "675";
        pair3[1] = "710";
        String[] pair4 = new String[2];
        pair4[0] = "1010";
        pair4[1] = "731";
        String[] pair5 = new String[2];
        pair5[0] = "932";
        pair5[1] = "732";
        expectedResult.add(pair1);
        expectedResult.add(pair2);
        expectedResult.add(pair3);
        expectedResult.add(pair4);
        expectedResult.add(pair5);
        //Act
        List<String[]> result = Main.getID_CountPairsFromResultFile(input);
        //Assert
        Assert.assertEquals(expectedResult.size(), result.size());
        for(int i=0;i<result.size();i++){
            Assert.assertEquals(expectedResult.get(i)[0], result.get(i)[0]);
            Assert.assertEquals(expectedResult.get(i)[1], result.get(i)[1]);
        }

    }

    @Test
    public void test_sortID_CountPairs() throws IOException {
        //Arrange
        List<String[]> input = Main.getID_CountPairsFromResultFile(new File("TextFiles/testResul.txt"));
        List<String[]> expectedResult = new ArrayList<>();
        String[] pair1 = new String[2];
        pair1[0] = "1010";
        pair1[1] = "731";
        String[] pair2 = new String[2];
        pair2[0] = "982";
        pair2[1] = "708";
        String[] pair3 = new String[2];
        pair3[0] = "932";
        pair3[1] = "732";
        String[] pair4 = new String[2];
        pair4[0] = "761";
        pair4[1] = "709";
        String[] pair5 = new String[2];
        pair5[0] = "675";
        pair5[1] = "710";
        expectedResult.add(pair1);
        expectedResult.add(pair2);
        expectedResult.add(pair3);
        expectedResult.add(pair4);
        expectedResult.add(pair5);
        //Act
        List<String[]> result=Main.sortID_CountPairs(input);
        //Assert
        Assert.assertEquals(expectedResult.size(), result.size());
        for(int i=0;i<result.size();i++){
            Assert.assertEquals(expectedResult.get(i)[0], result.get(i)[0]);
            Assert.assertEquals(expectedResult.get(i)[1], result.get(i)[1]);
        }
    }

    @Test
    public void test_generateFilteredLogFile() throws IOException {
        HashMap<String, String> event_module = new HashMap<>();
        String logFilePath = "TextFiles/testFiltered_log.txt";
        File input = new File("TextFiles/testInput.txt");
        String expectedResult = "708480810845104734";
        StringBuilder result= new StringBuilder();
        //Act
        File filteredLog=Main.generateFilteredLogFile(event_module, input, logFilePath);
        String line;
        BufferedReader br=new BufferedReader(new InputStreamReader(new FileInputStream(filteredLog)));
        while((line=br.readLine())!=null){
            result.append(line);
        }
        //Assert
        Assert.assertEquals(expectedResult, result.toString());


    }

    @Test
    public void test_executeAlgorithmAndGenerateResultFile() throws IOException {
        File testLog = new File("TextFiles/testFiltered_log2.txt");
        String expectedResult = "708 2734 11084 14808 15104 1";

        //Act
        File testResult = Main.executeAlgorithmAndGenerateResultFile(testLog);
        StringBuilder result=new StringBuilder();
        BufferedReader br=new BufferedReader(new InputStreamReader(new FileInputStream(testResult)));
        String line;
        while ((line=br.readLine())!=null){
            result.append(line);
        }
        //Assert
        Assert.assertEquals(expectedResult, result.toString());


    }

}
