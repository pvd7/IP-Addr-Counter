package ru.pvd.util;

import org.junit.Assert;
import org.junit.Test;

public class StringUtilTest {

    @Test
    public void formatNumber() {
        Assert.assertEquals("1", StringUtil.formatNumberUnderline(1));
        Assert.assertEquals("10", StringUtil.formatNumberUnderline(10));
        Assert.assertEquals("100", StringUtil.formatNumberUnderline(100));
        Assert.assertEquals("1_000", StringUtil.formatNumberUnderline(1000));
        Assert.assertEquals("10_000", StringUtil.formatNumberUnderline(10000));
        Assert.assertEquals("100_000", StringUtil.formatNumberUnderline(100000));
        Assert.assertEquals("1_000_000", StringUtil.formatNumberUnderline(1000000));
        Assert.assertEquals("10_000_000", StringUtil.formatNumberUnderline(10000000));
        Assert.assertEquals("100_000_000", StringUtil.formatNumberUnderline(100000000));
        Assert.assertEquals("1_000_000_000", StringUtil.formatNumberUnderline(1000000000));
    }

    @Test
    public void insertTextEveryFewCharacters() {
        String source = "qwertyuiop";
        String insert = "!!!"; 

        // forward
        var direction = StringUtil.Direction.FORWARD;
        Assert.assertEquals( "q!!!w!!!e!!!r!!!t!!!y!!!u!!!i!!!o!!!p", StringUtil.insertTextEveryFewCharacters(source, insert, 1, direction));
        Assert.assertEquals("qw!!!er!!!ty!!!ui!!!op", StringUtil.insertTextEveryFewCharacters(source, insert, 2, direction));
        Assert.assertEquals("qwe!!!rty!!!uio!!!p", StringUtil.insertTextEveryFewCharacters(source, insert, 3, direction));
        Assert.assertEquals("qwer!!!tyui!!!op", StringUtil.insertTextEveryFewCharacters(source, insert, 4, direction));
        Assert.assertEquals("qwert!!!yuiop", StringUtil.insertTextEveryFewCharacters(source, insert, 5, direction));
        Assert.assertEquals("qwerty!!!uiop", StringUtil.insertTextEveryFewCharacters(source, insert, 6, direction));
        Assert.assertEquals("qwertyu!!!iop", StringUtil.insertTextEveryFewCharacters(source, insert, 7, direction));
        Assert.assertEquals("qwertyui!!!op", StringUtil.insertTextEveryFewCharacters(source, insert, 8, direction));
        Assert.assertEquals("qwertyuio!!!p", StringUtil.insertTextEveryFewCharacters(source, insert, 9, direction));
        Assert.assertEquals(source, StringUtil.insertTextEveryFewCharacters(source, insert, 10, direction));
        Assert.assertEquals(source, StringUtil.insertTextEveryFewCharacters(source, insert, 11, direction));

        // back
        direction = StringUtil.Direction.BACK;
        Assert.assertEquals( "q!!!w!!!e!!!r!!!t!!!y!!!u!!!i!!!o!!!p", StringUtil.insertTextEveryFewCharacters(source, insert, 1, direction));
        Assert.assertEquals("qw!!!er!!!ty!!!ui!!!op", StringUtil.insertTextEveryFewCharacters(source, insert, 2, direction));
        Assert.assertEquals("q!!!wer!!!tyu!!!iop", StringUtil.insertTextEveryFewCharacters(source, insert, 3, direction));
        Assert.assertEquals("qw!!!erty!!!uiop", StringUtil.insertTextEveryFewCharacters(source, insert, 4, direction));
        Assert.assertEquals("qwert!!!yuiop", StringUtil.insertTextEveryFewCharacters(source, insert, 5, direction));
        Assert.assertEquals("qwer!!!tyuiop", StringUtil.insertTextEveryFewCharacters(source, insert, 6, direction));
        Assert.assertEquals("qwe!!!rtyuiop", StringUtil.insertTextEveryFewCharacters(source, insert, 7, direction));
        Assert.assertEquals("qw!!!ertyuiop", StringUtil.insertTextEveryFewCharacters(source, insert, 8, direction));
        Assert.assertEquals("q!!!wertyuiop", StringUtil.insertTextEveryFewCharacters(source, insert, 9, direction));
        Assert.assertEquals("qwertyuiop", StringUtil.insertTextEveryFewCharacters(source, insert, 10, direction));
        Assert.assertEquals("qwertyuiop", StringUtil.insertTextEveryFewCharacters(source, insert, 11, direction));
    }
}