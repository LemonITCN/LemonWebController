module.exports = function (grunt) {
    grunt.initConfig({
        pkg: grunt.file.readJSON('package.json'),
        watch: {
            jsWatch: {
                options: {livereload: true},
                files: ['code/**/*.js'],
                tasks: ['concat:build', 'uglify']
            }
        },
        concat: {
            build: {
                src: "code/**/*.js",
                dest: "SystemCommand.js"
            }
        },
        uglify: {
            options: {
                mangle: true,//混淆变量名
                perserveComments: 'false',
                banner: '/*! <%= pkg.name %> <%= grunt.template.today("dd-mm-yyyy") %> \n\n Lemonsoft Technology */\n\n'//设置JS顶部注释
            },
            build: {
                files: [{
                    "SystemCommand.min.js": ['<%= concat.build.dest %>']//输入输出文件
                }]
            }
        }
    });
    grunt.loadNpmTasks('grunt-contrib-concat');
    grunt.loadNpmTasks('grunt-contrib-uglify');
    grunt.loadNpmTasks('grunt-contrib-watch');
    grunt.registerTask('default', ['watch']);
};