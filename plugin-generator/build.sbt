/**
 * The SBT "bare" configuration is generally frowned upon at this point (https://github.com/sbt/sbt/issues/6217)
 * but since we want to leverage this configuration for both the root build and the meta build, this is the best
 * way I've found to share these settings with both.
 */

Compile / run / mainClass := Some("com.github.halfmatthalfcat.Generator")
Compile / packageBin / mainClass := Some("com.github.halfmatthalfcat.Generator")