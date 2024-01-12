package com.orion.ops.module.asset.meta;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.ValueFilter;
import com.orion.lang.utils.Colors;
import com.orion.lang.utils.Strings;
import com.orion.lang.utils.collect.Lists;
import com.orion.lang.utils.io.FileReaders;
import com.orion.lang.utils.io.Files1;
import lombok.Data;

import java.io.File;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 终端主题生成 __META__
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2023/12/7 10:52
 */
public class TerminalThemeGenerator {

    public static void main(String[] args) {
        List<File> files = Files1.listFiles("D:\\idea-project\\iTerm2-Color-Schemes\\vhs");
        // 过滤的 theme
        List<String> schemaFilter = Lists.of(
                "catppuccin-mocha", "MaterialDesignColors",
                "catppuccin-macchiato", "OneHalfDark",
                "Dracula", "Atom",
                "Apple System Colors", "Builtin Tango Light",
                "Duotone Dark", "BlulocoLight",
                "Chester", "CLRS",
                "Calamity", "Tomorrow"
        );
        // 颜色大写
        ValueFilter colorFilter = (Object object, String name, Object value) -> {
            if (value instanceof String && value.toString().contains("#")) {
                return ((String) value).toUpperCase();
            } else {
                return value;
            }
        };
        // 转换
        List<TerminalTheme> arr = files.stream()
                .filter(f -> Lists.isEmpty(schemaFilter) || schemaFilter.stream()
                        .map(s -> s + ".json")
                        .anyMatch(s -> f.getName().equalsIgnoreCase(s)))
                .map(f -> {
                    JSONObject schema = JSONObject.parseObject(new String(FileReaders.readAllBytes(f)));
                    // 设置选中背景色
                    schema.put("selectionBackground", schema.getString("selection"));
                    // 设置主题色
                    String background = schema.getString("background");
                    TerminalTheme theme = new TerminalTheme();
                    theme.setName(schema.getString("name"));
                    theme.setDark(Colors.isDarkColor(background));
                    // header 颜色为深 12
                    theme.setHeaderBackgroundColor(adjustColor(background, -12));
                    theme.setSchema(JSON.parseObject(JSON.toJSONString(schema), TerminalThemeSchema.class));
                    return theme;
                }).collect(Collectors.toList());
        // 排序
        if (!Lists.isEmpty(schemaFilter)) {
            arr.sort(Comparator.comparing(s -> schemaFilter.indexOf(s.getName())));
        }
        // 打印 json
        String json = JSON.toJSONString(arr, colorFilter);
        System.out.println("\n\n" + json);
    }

    /*
      var term = new Terminal();
      var doc = document.getElementById('themes');
      for (let t of themes) {
        var span = document.createElement('span');
        span.innerHTML = t.name;
        span.style.display = 'inline-block';
        span.style.padding = '4px 8px';
        span.style.margin = '4px';
        span.style.border = '1px solid green';
        if(t.dark) {
          span.style.background = '#000';
          span.style.color = '#FFF';
        }
        span.addEventListener('click', function() {
          term.setOption('theme', t);
          term.reset();
          for (let i = 0; i < 9; i++) {
            term.write('[' + (30 + i) + 'm');
            term.write('          ' + i + 'orion ops pro');
            term.write('[0m\r\n');
          }
          for (let i = 0; i < 9; i++) {
            term.write('[' + (90 + i) + 'm');
            term.write('          ' + i + 'orion ops pro');
            term.write('[0m\r\n');
          }
        });
        doc.append(span);
      }
      term.open(document.getElementById('terminal'));
     */

    @Data
    public static class TerminalTheme {
        @JSONField(ordinal = 0)
        private String name;
        @JSONField(ordinal = 1)
        private Boolean dark;
        @JSONField(ordinal = 2)
        private String headerBackgroundColor;
        @JSONField(ordinal = 3)
        private TerminalThemeSchema schema;
    }

    @Data
    public static class TerminalThemeSchema {
        @JSONField(ordinal = 0)
        private String background;
        @JSONField(ordinal = 1)
        private String foreground;
        @JSONField(ordinal = 2)
        private String cursor;
        @JSONField(ordinal = 3)
        private String selectionBackground;
        @JSONField(ordinal = 4)
        private String black;
        @JSONField(ordinal = 5)
        private String red;
        @JSONField(ordinal = 6)
        private String green;
        @JSONField(ordinal = 7)
        private String yellow;
        @JSONField(ordinal = 8)
        private String blue;
        @JSONField(ordinal = 9)
        private String magenta;
        @JSONField(ordinal = 10)
        private String cyan;
        @JSONField(ordinal = 11)
        private String white;
        @JSONField(ordinal = 12)
        private String brightBlack;
        @JSONField(ordinal = 13)
        private String brightRed;
        @JSONField(ordinal = 14)
        private String brightGreen;
        @JSONField(ordinal = 15)
        private String brightYellow;
        @JSONField(ordinal = 16)
        private String brightBlue;
        @JSONField(ordinal = 17)
        private String brightMagenta;
        @JSONField(ordinal = 18)
        private String brightCyan;
        @JSONField(ordinal = 19)
        private String brightWhite;
    }

    /**
     * 调整颜色
     *
     * @param color color
     * @param range 正数越浅 负数越深
     * @return color
     */
    private static String adjustColor(String color, int range) {
        StringBuilder newColor = new StringBuilder("#");
        for (int i = 0; i < 3; i++) {
            int c = Integer.parseInt(color.substring(i * 2 + 1, i * 2 + 3), 16);
            c += range;
            if (c < 0) {
                c = 0;
            } else if (c > 255) {
                c = 255;
            }
            newColor.append(Strings.leftPad(Integer.toString(c, 16), 2, "0"));
        }
        return newColor.toString();
    }

}
