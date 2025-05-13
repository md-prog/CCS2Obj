# CCS2Obj
Java project that can read `.ccs` files ( from `.G.U/.hack` series) and convert them to `.obj` files.

The `.ccs` files in the `.hack` series are the container format for the 3D models used in the games.

They have an internal structure involving clumps and objects, support various types of models (rigid, deformable, shadow, morph target), and store essential data like vertex positions, texture coordinates, material information, and, in later versions, bind pose data for animation.
Tools like [StudioCCS](https://github.com/NCDyson/StudioCCS) are vital for interacting with these proprietary files.

This project is to create a Java-based tool that can read `.ccs` files from `.hack//G.U.` (the trilogy consisting of Rebirth, Reminisce, and Redemption, and later compiled in Last Recode) and convert them into standard `.obj` files.

See https://forum.xentax.com/viewtopic.php?f=16&t=16010

