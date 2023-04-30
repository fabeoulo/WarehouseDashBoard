/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
var path = require('path');

module.exports = function (grunt) {
    // Project configuration.
    grunt.initConfig({
		pkg: grunt.file.readJSON('package.json'),
        bower: {
            install: {
                options: {
                    copy: true,
                    targetDir: './src/main/webapp/libs',
                    layout: 'byType',
                    install: true,
                    verbose: false,
                    prune: false,
                    cleanTargetDir: false,
                    cleanBowerDir: false,
                    bowerOptions: {}
                }
            }
        }
    });
    grunt.loadNpmTasks('grunt-bower-task');
};
