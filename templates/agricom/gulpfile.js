'use strict';

var gulp         = require('gulp'),
	watch        = require('gulp-watch'),
	uglify       = require('gulp-uglify'),
	sass         = require('gulp-sass'),
	postcss      = require('gulp-postcss'),
	mqpacker     = require('css-mqpacker'),
	cleanCSS     = require('gulp-clean-css'),
	autoprefixer = require('autoprefixer'),
	changed      = require('gulp-changed'),
	reference    = require('gulp-reference'),
	rename       = require('gulp-rename'),
	sourcemaps   = require('gulp-sourcemaps'),
	fileinclude  = require('gulp-file-include'),
	imagemin     = require('gulp-imagemin'),
	pngquant     = require('imagemin-pngquant'),
	rimraf       = require('rimraf'),

	path = {
		dist: {
			html:   'dist/',
			js:     'dist/js/',
			style:  'dist/css/',
			img:    'dist/img/',
			fonts:  'dist/fonts/'
		},
		src: {
			html:  ['src/html/**/*.html','!src/html/template/*.html'],
			js:    ['src/js/lib/device.js', 'src/js/lib/jquery-2.2.4.js', 'src/js/main.js'],
			style:  'src/style/main.scss',
			img:    'src/img/**/*.*',
			fonts:  'src/fonts/**/*.*'
		},
		watch: {
			html:   'src/html/**/*.html',
			js:     'src/js/**/*.js',
			style:  'src/style/**/*.{css,scss}',
			img:    'src/img/**/*.*',
			fonts:  'src/fonts/**/*.*'
		},
		clean: './dist'
	};

var postCssPlugins = [
	autoprefixer({
		browsers: ['last 25 versions', '> 1%', 'ie 9']
	}),
	mqpacker({
		sort: true
	}),
];

gulp.task('clean', function (cb) {
	rimraf(path.clean, cb);
});

gulp.task('html:build', function () {
	return gulp.src(path.src.html)
		.pipe(fileinclude({
			prefix: '@@',
			basepath: '@file',
			indent: true
		}))
		.pipe(gulp.dest(path.dist.html));
});

gulp.task('js:build', function ()
{
	return gulp.src(path.src.js)
		.pipe(sourcemaps.init())
		.pipe(reference())
		.pipe(gulp.dest(path.dist.js))
		.pipe(uglify())
		.pipe(rename({suffix: ".min"}))
		.pipe(sourcemaps.write('.'))
		.pipe(gulp.dest(path.dist.js));
});

gulp.task('style:build', function ()
{
	return gulp.src(path.src.style)
		.pipe(sourcemaps.init())
		.pipe(reference())
		.pipe(sass({outputStyle: 'compact'}).on('error', sass.logError))
		.pipe(cleanCSS({
			format: 'beautify',
			level: 2
		}))
		.pipe(postcss(postCssPlugins))
		.pipe(rename({basename: "style"}))
		.pipe(gulp.dest(path.dist.style))
		.pipe(cleanCSS())
		.pipe(rename({suffix: ".min"}))
		.pipe(sourcemaps.write('.'))
		.pipe(gulp.dest(path.dist.style));
});

gulp.task('image:build', function ()
{
	return gulp.src(path.src.img)
		.pipe(imagemin({
			optimizationLevel: 5,
			progressive: true,
			svgoPlugins: [{removeViewBox: false}],
			use: [pngquant()],
			interlaced: true
		}))
		.pipe(gulp.dest(path.dist.img));
});

gulp.task('fonts:build', function()
{
	return gulp.src(path.src.fonts)
		.pipe(changed(path.dist.fonts))
		.pipe(gulp.dest(path.dist.fonts))
});


gulp.task('js:dev', function ()
{
	return gulp.src(path.src.js)
		.pipe(sourcemaps.init())
		.pipe(reference())
		.pipe(rename({suffix: ".min"}))
		.pipe(sourcemaps.write('.'))
		.pipe(gulp.dest(path.dist.js));
});

gulp.task('style:dev', function ()
{
	return gulp.src(path.src.style)
		.pipe(sourcemaps.init())
		.pipe(reference())
		.pipe(sass().on('error', sass.logError))
		.pipe(postcss(postCssPlugins))
		.pipe(cleanCSS({
			format: 'beautify',
			level: 2
		}))
		.pipe(rename({
			basename: "style",
			suffix: ".min"
		}))
		.pipe(sourcemaps.write('.'))
		.pipe(gulp.dest(path.dist.style));
});

gulp.task('image:dev', function ()
{
	return gulp.src(path.src.img)
		.pipe(changed(path.dist.img))
		.pipe(gulp.dest(path.dist.img));
});

gulp.task('build', [
	'html:build',
	'js:build',
	'style:build',
	'fonts:build',
	'image:build'
]);

gulp.task('dev', [
	'clean',
	'html:build',
	'js:dev',
	'style:dev',
	'fonts:build',
	'image:dev'
]);

gulp.task('default', ['clean'], function ()
{
	return gulp.start('build');
});

gulp.task('watch', function (_cb)
{
	watch(path.watch.html, function(event, cb)
	{
		gulp.start('html:build');
	});
	watch(path.watch.style, function(event, cb)
	{
		gulp.start('style:dev');
	});
	watch(path.watch.js, function(event, cb)
	{
		gulp.start('js:dev');
	});
	watch(path.watch.img, function(event, cb)
	{
		gulp.start('image:dev');
	});
	watch(path.watch.fonts, function(event, cb)
	{
		gulp.start('fonts:build');
	});

	_cb();
});